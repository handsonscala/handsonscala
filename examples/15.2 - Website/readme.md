# Example 15.2 - Website
Database-backed websocket chat website

```bash
./mill -i app.test
```

## Upstream Example: [14.4 - Websockets](https://github.com/handsonscala/handsonscala/tree/v1/examples/14.4%20-%20Websockets):
Diff:
```diff
diff --git a/14.4 - Websockets/app/src/MinimalApplication.scala b/15.2 - Website/app/src/MinimalApplication.scala
index d5614ab..f7f07fd 100644
--- a/14.4 - Websockets/app/src/MinimalApplication.scala	
+++ b/15.2 - Website/app/src/MinimalApplication.scala	
@@ -1,7 +1,24 @@
 package app
 import scalatags.Text.all._
 object MinimalApplication extends cask.MainRoutes {
-  var messages = Vector(("alice", "Hello World!"), ("bob", "I am cow, hear me moo"))
+  case class Message(name: String, msg: String)
+  import com.opentable.db.postgres.embedded.EmbeddedPostgres
+  val server = EmbeddedPostgres.builder()
+    .setDataDirectory(System.getProperty("user.home") + "/data")
+    .setCleanDataDirectory(false).setPort(5432)
+    .start()
+  import io.getquill._
+  import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
+  val pgDataSource = new org.postgresql.ds.PGSimpleDataSource()
+  pgDataSource.setUser("postgres")
+  val hikariConfig = new HikariConfig()
+  hikariConfig.setDataSource(pgDataSource)
+  val ctx = new PostgresJdbcContext(LowerCase, new HikariDataSource(hikariConfig))
+  ctx.executeAction("CREATE TABLE IF NOT EXISTS message (name text, msg text);")
+  import ctx._
+
+  def messages = ctx.run(query[Message].map(m => (m.name, m.msg)))
+
   var openConnections = Set.empty[cask.WsChannelActor]
   val bootstrap = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
 
@@ -37,7 +54,7 @@ object MinimalApplication extends cask.MainRoutes {
     if (name == "") ujson.Obj("success" -> false, "err" -> "Name cannot be empty")
     else if (msg == "") ujson.Obj("success" -> false, "err" -> "Message cannot be empty")
     else {
-      messages = messages :+ (name -> msg)
+      ctx.run(query[Message].insert(lift(Message(name, msg))))
       for (conn <- openConnections) conn.send(cask.Ws.Text(messageList().render))
       ujson.Obj("success" -> true, "err" -> "")
     }
diff --git a/14.4 - Websockets/app/test/src/ExampleTests.scala b/15.2 - Website/app/test/src/ExampleTests.scala
index 15c1c74..1e93cb2 100644
--- a/14.4 - Websockets/app/test/src/ExampleTests.scala	
+++ b/15.2 - Website/app/test/src/ExampleTests.scala	
@@ -7,18 +7,19 @@ import castor.Context.Simple.global, cask.util.Logger.Console._
 object ExampleTests extends TestSuite {
   def withServer[T](example: cask.main.Main)(f: String => T): T = {
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
   }
 
   val tests = Tests {
-    test("success") - withServer(MinimalApplication) { host =>
+    test("success") - {
+      withServer(MinimalApplication) { host =>
         var wsPromise = scala.concurrent.Promise[String]
         val wsClient = cask.util.WsClient.connect(s"$host/subscribe") {
           case cask.Ws.Text(msg) => wsPromise.success(msg)
@@ -26,19 +27,10 @@ object ExampleTests extends TestSuite {
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
 
@@ -48,24 +40,25 @@ object ExampleTests extends TestSuite {
 
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
       val response1 = requests.post(host, data = ujson.Obj("name" -> "haoyi"), check = false)
       assert(response1.statusCode == 400)
diff --git a/14.4 - Websockets/build.sc b/15.2 - Website/build.sc
index 5995abe..dd5f2f5 100644
--- a/14.4 - Websockets/build.sc	
+++ b/15.2 - Website/build.sc	
@@ -3,6 +3,9 @@ import mill._, scalalib._
 object app extends ScalaModule {
   def scalaVersion = "2.13.2"
   def ivyDeps = Agg(
+    ivy"io.getquill::quill-jdbc:3.5.0",
+    ivy"org.postgresql:postgresql:42.2.8",
+    ivy"com.opentable.components:otj-pg-embedded:0.13.1",
     ivy"com.lihaoyi::scalatags:0.9.1",
     ivy"com.lihaoyi::cask:0.6.7"
   )
```
## Downstream Examples

- [15.4 - WebsiteTimestamps](https://github.com/handsonscala/handsonscala/tree/v1/examples/15.4%20-%20WebsiteTimestamps)