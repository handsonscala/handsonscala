# Example 15.5 - ThreadedChat
Database-backed websocket threaded-chat website

```bash
./mill -i app.test
```

## Upstream Example: [14.4 - Websockets](https://github.com/handsonscala/handsonscala/tree/v2/examples/14.4%20-%20Websockets):
Diff:
```diff
diff --git a/14.4 - Websockets/app/resources/static/app.js b/15.5 - ThreadedChat/app/resources/static/app.js
index efb34ca..172c38d 100644
--- a/14.4 - Websockets/app/resources/static/app.js	
+++ b/15.5 - ThreadedChat/app/resources/static/app.js	
@@ -1,7 +1,9 @@
 function submitForm() {
   fetch(
     "/",
-    {method: "POST", body: JSON.stringify({name: nameInput.value, msg: msgInput.value})}
+    {method: "POST", body: JSON.stringify({
+        parent: parentInput.value, name: nameInput.value, msg: msgInput.value
+    })}
   ).then(response => response.json())
    .then(json => {
     if (json["success"]) msgInput.value = ""
diff --git a/14.4 - Websockets/app/src/MinimalApplication.scala b/15.5 - ThreadedChat/app/src/MinimalApplication.scala
index 63d571a..c46fd46 100644
--- a/14.4 - Websockets/app/src/MinimalApplication.scala	
+++ b/15.5 - ThreadedChat/app/src/MinimalApplication.scala	
@@ -1,7 +1,36 @@
 package app
 import scalatags.Text.all.*
+import scalasql.simple.*, PostgresDialect.*
 object MinimalApplication extends cask.MainRoutes:
-  var messages = Vector(("alice", "Hello World!"), ("bob", "I am cow, hear me moo"))
+  case class Message(id: Int, parent: Option[Int], name: String, msg: String)
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
+    CREATE TABLE IF NOT EXISTS message (id serial, parent integer, name text, msg text);"""
+  )
+
+  def messages = db.run(Message.select)
+
   var openConnections = Set.empty[cask.WsChannelActor]
   val bootstrap = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
 
@@ -21,6 +50,7 @@ object MinimalApplication extends cask.MainRoutes:
           div(id := "messageList")(messageList()),
           div(id := "errorDiv", color.red),
           form(onsubmit := "submitForm(); return false")(
+            input(`type` := "text", id := "parentInput", placeholder := "Reply To (Optional)"),
             input(`type` := "text", id := "nameInput", placeholder := "User name"),
             input(`type` := "text", id := "msgInput", placeholder := "Write a message!"),
             input(`type` := "submit")
@@ -30,16 +60,23 @@ object MinimalApplication extends cask.MainRoutes:
     )
   )
 
-  def messageList() = frag(for (name, msg) <- messages yield p(b(name), " ", msg))
+  def messageList(): Frag =
+    val msgMap = messages.groupBy(_.parent)
+    def messageListFrag(parent: Option[Int] = None): Frag = frag(
+      for msg <- msgMap.getOrElse(parent, Nil) yield div(
+        p("#", msg.id, " ", b(msg.name), " ", msg.msg),
+        div(paddingLeft := 25)(messageListFrag(Some(msg.id)))
+      )
+    )
+    messageListFrag(None)
 
   @cask.postJson("/")
-  def postChatMsg(name: String, msg: String) =
-    if name == "" then
-      ujson.Obj("success" -> false, "err" -> "Name cannot be empty")
-    else if msg == "" then
-      ujson.Obj("success" -> false, "err" -> "Message cannot be empty")
+  def postChatMsg(parent: String, name: String, msg: String) =
+    if name == "" then ujson.Obj("success" -> false, "err" -> "Name cannot be empty")
+    else if msg == "" then ujson.Obj("success" -> false, "err" -> "Message cannot be empty")
     else
-      messages = messages :+ (name -> msg)
+      val parentInt = parent match{case "" => None case n => Some(n.toInt)}
+      db.run(Message.insert.columns(_.parent := parentInt, _.name := name, _.msg := msg))
       for conn <- openConnections do conn.send(cask.Ws.Text(messageList().render))
       ujson.Obj("success" -> true, "err" -> "")
 
diff --git a/14.4 - Websockets/app/test/src/ExampleTests.scala b/15.5 - ThreadedChat/app/test/src/ExampleTests.scala
index 1ca626d..423910c 100644
--- a/14.4 - Websockets/app/test/src/ExampleTests.scala	
+++ b/15.5 - ThreadedChat/app/test/src/ExampleTests.scala	
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
 
   val tests = Tests:
-    test("success") - withServer(MinimalApplication) { host =>
+    test("success") - {
+      withServer(MinimalApplication) { host =>
         var wsPromise = scala.concurrent.Promise[String]
         val wsHost = host.replace("http", "ws")
         val wsClient = cask.util.WsClient.connect(s"$wsHost/subscribe"):
@@ -25,21 +26,12 @@ object ExampleTests extends TestSuite:
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
-      val response = requests.post(host, data = ujson.Obj("name" -> "haoyi", "msg" -> "Test Message!"))
+        val response = requests.post(host, data = ujson.Obj("parent" -> "", "name" -> "haoyi", "msg" -> "Test Message!"))
 
         val parsed = ujson.read(response)
         assert(parsed("success") == ujson.True)
@@ -47,39 +39,29 @@ object ExampleTests extends TestSuite:
 
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
+        assert(success2.text().contains("#1"))
         assert(success2.text().contains("haoyi"))
         assert(success2.text().contains("Test Message!"))
+        assert(!success2.text().contains("#2"))
         assert(success2.statusCode == 200)
+        wsPromise = scala.concurrent.Promise[String]
+        val response2 = requests.post(host, data = ujson.Obj("parent" -> "1", "name" -> "haoyi", "msg" -> "Test Reply!"))
       }
-    test("failure") - withServer(MinimalApplication) { host =>
-      val response1 = requests.post(host, data = (ujson.Obj("name" -> "haoyi"): requests.RequestBlob), check = false)
-      assert(response1.statusCode == 400)
-      val response2 = requests.post(host, data = ujson.Obj("name" -> "haoyi", "msg" -> ""))
-      assert(
-        ujson.read(response2) ==
-        ujson.Obj("success" -> false, "err" -> "Message cannot be empty")
-      )
-      val response3 = requests.post(host, data = ujson.Obj("name" -> "", "msg" -> "Test Message!"))
-      assert(
-        ujson.read(response3) ==
-        ujson.Obj("success" -> false, "err" -> "Name cannot be empty")
-      )
+      withServer(MinimalApplication) { host =>
+        val success = requests.get(host)
+
+        assert(success.text().contains("Scala Chat!"))
+        assert(success.text().contains("haoyi"))
+        assert(success.text().contains("#1"))
+        assert(success.text().contains("Test Message!"))
+        assert(success.text().contains("#2"))
+        assert(success.text().contains("Test Reply!"))
+        assert(success.statusCode == 200)
       }
-    test("javascript") - withServer(MinimalApplication) { host =>
-      val response1 = requests.get(host + "/static/app.js")
-      assert(response1.text().contains("function submitForm()"))
     }
diff --git a/14.4 - Websockets/build.mill b/15.5 - ThreadedChat/build.mill
index ce45b51..987658a 100644
--- a/14.4 - Websockets/build.mill	
+++ b/15.5 - ThreadedChat/build.mill	
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
