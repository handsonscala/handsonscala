# Example 15.2 - Website
Database-backed websocket chat website

```bash
./mill -i app.test
```

## Upstream Example: [14.4 - Websockets](https://github.com/handsonscala/handsonscala/tree/v2/examples/14.4%20-%20Websockets):
Diff:
```diff
diff --git a/14.4 - Websockets/app/src/MinimalApplication.scala b/15.2 - Website/app/src/MinimalApplication.scala
index 63d571a..be9cfeb 100644
--- a/14.4 - Websockets/app/src/MinimalApplication.scala	
+++ b/15.2 - Website/app/src/MinimalApplication.scala	
@@ -1,7 +1,35 @@
 package app
 import scalatags.Text.all.*
+import scalasql.simple.*, PostgresDialect.*
 object MinimalApplication extends cask.MainRoutes:
-  var messages = Vector(("alice", "Hello World!"), ("bob", "I am cow, hear me moo"))
+  case class Message(name: String, msg: String)
+  object Message extends SimpleTable[Message]
+  import io.zonky.test.db.postgres.embedded.EmbeddedPostgres
+
+  // Start the database on a best-effort basis, in case some other
+  // process is already running it
+  val server = EmbeddedPostgres.builder()
+    .setDataDirectory(System.getProperty("user.home") + "/data")
+    .setCleanDataDirectory(false).setPort(5432)
+    .start()
+
+  val pgDataSource = org.postgresql.ds.PGSimpleDataSource()
+  pgDataSource.setUser("postgres")
+  val client = scalasql.DbClient.DataSource(
+    pgDataSource,
+    config = new scalasql.Config {
+      override def nameMapper(v: String) = v.toLowerCase
+    }
+  )
+
+  val db = client.getAutoCommitClientConnection
+  sys.addShutdownHook{ db.close() }
+  db.updateRaw(
+    """DROP TABLE IF EXISTS message;
+    CREATE TABLE IF NOT EXISTS message (name text, msg text);""")
+
+  def messages = db.run(Message.select.map(m => (m.name, m.msg)))
+
   var openConnections = Set.empty[cask.WsChannelActor]
   val bootstrap = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
 
@@ -30,7 +58,7 @@ object MinimalApplication extends cask.MainRoutes:
     )
   )
 
-  def messageList() = frag(for (name, msg) <- messages yield p(b(name), " ", msg))
+  def messageList() = frag(for ((name, msg) <- messages) yield p(b(name), " ", msg))
 
   @cask.postJson("/")
   def postChatMsg(name: String, msg: String) =
@@ -39,7 +67,7 @@ object MinimalApplication extends cask.MainRoutes:
     else if msg == "" then
       ujson.Obj("success" -> false, "err" -> "Message cannot be empty")
     else
-      messages = messages :+ (name -> msg)
+      db.run(Message.insert.values(Message(name, msg)))
       for conn <- openConnections do conn.send(cask.Ws.Text(messageList().render))
       ujson.Obj("success" -> true, "err" -> "")
 
diff --git a/14.4 - Websockets/app/test/src/ExampleTests.scala b/15.2 - Website/app/test/src/ExampleTests.scala
index 1ca626d..4140eed 100644
--- a/14.4 - Websockets/app/test/src/ExampleTests.scala	
+++ b/15.2 - Website/app/test/src/ExampleTests.scala	
@@ -1,23 +1,24 @@
 package app
 
-import utest._
-import scala.concurrent._, duration.Duration.Inf
-import castor.Context.Simple.global, cask.util.Logger.Console._
+import utest.*
+import scala.concurrent.*, duration.Duration.Inf
+import castor.Context.Simple.global, cask.util.Logger.Console.*
 
 object ExampleTests extends TestSuite:
   def withServer[T](example: cask.main.Main)(f: String => T): T =
     val server = io.undertow.Undertow.builder
-      .addHttpListener(8084, "localhost")
+      .addHttpListener(8087, "localhost")
       .setHandler(example.defaultHandler)
       .build
     server.start()
     val res =
-      try f("http://localhost:8084")
+      try f("http://localhost:8087")
       finally server.stop()
     res
 
-  val tests = Tests:
-    test("success") - withServer(MinimalApplication) { host =>
+  val tests = Tests {
+    test("success") - {
+      withServer(MinimalApplication) { host =>
         var wsPromise = scala.concurrent.Promise[String]
         val wsHost = host.replace("http", "ws")
         val wsClient = cask.util.WsClient.connect(s"$wsHost/subscribe"):
@@ -25,19 +26,10 @@ object ExampleTests extends TestSuite:
         val success = requests.get(host)
 
         assert(success.text().contains("Scala Chat!"))
-      assert(success.text().contains("alice"))
-      assert(success.text().contains("Hello World!"))
-      assert(success.text().contains("bob"))
-      assert(success.text().contains("I am cow, hear me moo"))
         assert(success.statusCode == 200)
 
         val wsMsg = Await.result(wsPromise.future, Inf)
 
-      assert(wsMsg.contains("alice"))
-      assert(wsMsg.contains("Hello World!"))
-      assert(wsMsg.contains("bob"))
-      assert(wsMsg.contains("I am cow, hear me moo"))
-
         wsPromise = scala.concurrent.Promise[String]
         val response = requests.post(host, data = ujson.Obj("name" -> "haoyi", "msg" -> "Test Message!"))
 
@@ -47,24 +39,25 @@ object ExampleTests extends TestSuite:
 
         assert(response.statusCode == 200)
         val wsMsg2 = Await.result(wsPromise.future, Inf)
-      assert(wsMsg2.contains("alice"))
-      assert(wsMsg2.contains("Hello World!"))
-      assert(wsMsg2.contains("bob"))
-      assert(wsMsg2.contains("I am cow, hear me moo"))
         assert(wsMsg2.contains("haoyi"))
         assert(wsMsg2.contains("Test Message!"))
 
         val success2 = requests.get(host)
 
         assert(success2.text().contains("Scala Chat!"))
-      assert(success2.text().contains("alice"))
-      assert(success2.text().contains("Hello World!"))
-      assert(success2.text().contains("bob"))
-      assert(success2.text().contains("I am cow, hear me moo"))
         assert(success2.text().contains("haoyi"))
         assert(success2.text().contains("Test Message!"))
         assert(success2.statusCode == 200)
       }
+      withServer(MinimalApplication) { host =>
+        val success = requests.get(host)
+
+        assert(success.text().contains("Scala Chat!"))
+        assert(success.text().contains("haoyi"))
+        assert(success.text().contains("Test Message!"))
+        assert(success.statusCode == 200)
+      }
+    }
     test("failure") - withServer(MinimalApplication) { host =>
       val response1 = requests.post(host, data = (ujson.Obj("name" -> "haoyi"): requests.RequestBlob), check = false)
       assert(response1.statusCode == 400)
@@ -83,3 +76,4 @@ object ExampleTests extends TestSuite:
       val response1 = requests.get(host + "/static/app.js")
       assert(response1.text().contains("function submitForm()"))
     }
+  }
diff --git a/14.4 - Websockets/build.mill b/15.2 - Website/build.mill
index ce45b51..987658a 100644
--- a/14.4 - Websockets/build.mill	
+++ b/15.2 - Website/build.mill	
@@ -4,11 +4,14 @@ import mill.*, scalalib.*
 object app extends ScalaModule:
   def scalaVersion = "3.8.0-RC4"
   def mvnDeps = Seq(
+    mvn"com.lihaoyi::scalasql-simple::0.2.7",
+    mvn"org.postgresql:postgresql:42.7.8",
+    mvn"io.zonky.test:embedded-postgres:2.1.1",
     mvn"com.lihaoyi::scalatags:0.13.1",
     mvn"com.lihaoyi::cask:0.11.3"
   )
-  object test extends ScalaTests with TestModule.Utest:
 
+  object test extends ScalaTests with TestModule.Utest:
     def mvnDeps = Seq(
       mvn"com.lihaoyi::utest:0.9.4",
       mvn"com.lihaoyi::requests:0.9.0"
```
## Downstream Examples

- [15.4 - WebsiteTimestamps](https://github.com/handsonscala/handsonscala/tree/v2/examples/15.4%20-%20WebsiteTimestamps)
- [15.6 - ListenNotify](https://github.com/handsonscala/handsonscala/tree/v2/examples/15.6%20-%20ListenNotify)