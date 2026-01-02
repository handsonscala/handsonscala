# Example 15.4 - WebsiteTimestamps
Database-backed websocket chat website with timestamps on every chat message

```bash
./mill -i app.test
```

## Upstream Example: [15.2 - Website](https://github.com/handsonscala/handsonscala/tree/v2/examples/15.2%20-%20Website):
Diff:
```diff
diff --git a/15.2 - Website/app/src/MinimalApplication.scala b/15.4 - WebsiteTimestamps/app/src/MinimalApplication.scala
index be9cfeb..e62b7e7 100644
--- a/15.2 - Website/app/src/MinimalApplication.scala	
+++ b/15.4 - WebsiteTimestamps/app/src/MinimalApplication.scala	
@@ -2,7 +2,7 @@ package app
 import scalatags.Text.all.*
 import scalasql.simple.*, PostgresDialect.*
 object MinimalApplication extends cask.MainRoutes:
-  case class Message(name: String, msg: String)
+  case class Message(name: String, msg: String, time: Long)
   object Message extends SimpleTable[Message]
   import io.zonky.test.db.postgres.embedded.EmbeddedPostgres
 
@@ -24,11 +24,9 @@ object MinimalApplication extends cask.MainRoutes:
 
   val db = client.getAutoCommitClientConnection
   sys.addShutdownHook{ db.close() }
-  db.updateRaw(
-    """DROP TABLE IF EXISTS message;
-    CREATE TABLE IF NOT EXISTS message (name text, msg text);""")
+  db.updateRaw("DROP TABLE IF EXISTS message; CREATE TABLE IF NOT EXISTS message (name text, msg text, time bigint);")
 
-  def messages = db.run(Message.select.map(m => (m.name, m.msg)))
+  def messages = db.run(Message.select.map(m => (m.name, m.msg, m.time)))
 
   var openConnections = Set.empty[cask.WsChannelActor]
   val bootstrap = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
@@ -58,16 +56,21 @@ object MinimalApplication extends cask.MainRoutes:
     )
   )
 
-  def messageList() = frag(for ((name, msg) <- messages) yield p(b(name), " ", msg))
+  def messageList() = frag(
+    for (name, msg, time) <- messages
+    yield p(
+      java.time.Instant.ofEpochMilli(time).toString, " ",
+      b(name), " ",
+      msg
+    )
+  )
 
   @cask.postJson("/")
   def postChatMsg(name: String, msg: String) =
-    if name == "" then
-      ujson.Obj("success" -> false, "err" -> "Name cannot be empty")
-    else if msg == "" then
-      ujson.Obj("success" -> false, "err" -> "Message cannot be empty")
+    if name == "" then ujson.Obj("success" -> false, "err" -> "Name cannot be empty")
+    else if msg == "" then ujson.Obj("success" -> false, "err" -> "Message cannot be empty")
     else
-      db.run(Message.insert.values(Message(name, msg)))
+      db.run(Message.insert.values(Message(name, msg, System.currentTimeMillis())))
       for conn <- openConnections do conn.send(cask.Ws.Text(messageList().render))
       ujson.Obj("success" -> true, "err" -> "")
 
diff --git a/15.2 - Website/app/test/src/ExampleTests.scala b/15.4 - WebsiteTimestamps/app/test/src/ExampleTests.scala
index 4140eed..86be1d5 100644
--- a/15.2 - Website/app/test/src/ExampleTests.scala	
+++ b/15.4 - WebsiteTimestamps/app/test/src/ExampleTests.scala	
@@ -7,16 +7,16 @@ import castor.Context.Simple.global, cask.util.Logger.Console.*
 object ExampleTests extends TestSuite:
   def withServer[T](example: cask.main.Main)(f: String => T): T =
     val server = io.undertow.Undertow.builder
-      .addHttpListener(8087, "localhost")
+      .addHttpListener(8088, "localhost")
       .setHandler(example.defaultHandler)
       .build
     server.start()
     val res =
-      try f("http://localhost:8087")
+      try f("http://localhost:8088")
       finally server.stop()
     res
 
-  val tests = Tests {
+  val tests = Tests:
     test("success") - {
       withServer(MinimalApplication) { host =>
         var wsPromise = scala.concurrent.Promise[String]
@@ -41,12 +41,14 @@ object ExampleTests extends TestSuite:
         val wsMsg2 = Await.result(wsPromise.future, Inf)
         assert(wsMsg2.contains("haoyi"))
         assert(wsMsg2.contains("Test Message!"))
+        assert(wsMsg2.contains("2025-"))
 
         val success2 = requests.get(host)
 
         assert(success2.text().contains("Scala Chat!"))
         assert(success2.text().contains("haoyi"))
         assert(success2.text().contains("Test Message!"))
+        assert(success2.text().contains("2025-"))
         assert(success2.statusCode == 200)
       }
       withServer(MinimalApplication) { host =>
@@ -76,4 +78,3 @@ object ExampleTests extends TestSuite:
       val response1 = requests.get(host + "/static/app.js")
       assert(response1.text().contains("function submitForm()"))
     }
-  }
```
