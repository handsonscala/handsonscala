# Example 15.4 - WebsiteTimestamps
Database-backed websocket chat website with timestamps on every chat message

```bash
./mill -i app.test
```

## Upstream Example: [15.2 - Website](https://github.com/handsonscala/handsonscala/tree/v1/examples/15.2%20-%20Website):
Diff:
```diff
diff --git a/15.2 - Website/app/src/MinimalApplication.scala b/15.4 - WebsiteTimestamps/app/src/MinimalApplication.scala
index f7f07fd..6b15d02 100644
--- a/15.2 - Website/app/src/MinimalApplication.scala	
+++ b/15.4 - WebsiteTimestamps/app/src/MinimalApplication.scala	
@@ -1,7 +1,7 @@
 package app
 import scalatags.Text.all._
 object MinimalApplication extends cask.MainRoutes {
-  case class Message(name: String, msg: String)
+  case class Message(name: String, msg: String, time: Long)
   import com.opentable.db.postgres.embedded.EmbeddedPostgres
   val server = EmbeddedPostgres.builder()
     .setDataDirectory(System.getProperty("user.home") + "/data")
@@ -14,10 +14,10 @@ object MinimalApplication extends cask.MainRoutes {
   val hikariConfig = new HikariConfig()
   hikariConfig.setDataSource(pgDataSource)
   val ctx = new PostgresJdbcContext(LowerCase, new HikariDataSource(hikariConfig))
-  ctx.executeAction("CREATE TABLE IF NOT EXISTS message (name text, msg text);")
+  ctx.executeAction("CREATE TABLE IF NOT EXISTS message (name text, msg text, time bigint);")
   import ctx._
 
-  def messages = ctx.run(query[Message].map(m => (m.name, m.msg)))
+  def messages = ctx.run(query[Message].map(m => (m.name, m.msg, m.time)))
 
   var openConnections = Set.empty[cask.WsChannelActor]
   val bootstrap = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
@@ -47,14 +47,21 @@ object MinimalApplication extends cask.MainRoutes {
     )
   )
 
-  def messageList() = frag(for ((name, msg) <- messages) yield p(b(name), " ", msg))
+  def messageList() = frag(
+    for ((name, msg, time) <- messages)
+    yield p(
+      java.time.Instant.ofEpochMilli(time).toString, " ",
+      b(name), " ",
+      msg
+    )
+  )
 
   @cask.postJson("/")
   def postChatMsg(name: String, msg: String) = {
     if (name == "") ujson.Obj("success" -> false, "err" -> "Name cannot be empty")
     else if (msg == "") ujson.Obj("success" -> false, "err" -> "Message cannot be empty")
     else {
-      ctx.run(query[Message].insert(lift(Message(name, msg))))
+      ctx.run(query[Message].insert(lift(Message(name, msg, System.currentTimeMillis()))))
       for (conn <- openConnections) conn.send(cask.Ws.Text(messageList().render))
       ujson.Obj("success" -> true, "err" -> "")
     }
diff --git a/15.2 - Website/app/test/src/ExampleTests.scala b/15.4 - WebsiteTimestamps/app/test/src/ExampleTests.scala
index 1e93cb2..8e28e32 100644
--- a/15.2 - Website/app/test/src/ExampleTests.scala	
+++ b/15.4 - WebsiteTimestamps/app/test/src/ExampleTests.scala	
@@ -7,12 +7,12 @@ import castor.Context.Simple.global, cask.util.Logger.Console._
 object ExampleTests extends TestSuite {
   def withServer[T](example: cask.main.Main)(f: String => T): T = {
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
   }
@@ -42,12 +42,14 @@ object ExampleTests extends TestSuite {
         val wsMsg2 = Await.result(wsPromise.future, Inf)
         assert(wsMsg2.contains("haoyi"))
         assert(wsMsg2.contains("Test Message!"))
+        assert(wsMsg2.contains("2020-"))
 
         val success2 = requests.get(host)
 
         assert(success2.text().contains("Scala Chat!"))
         assert(success2.text().contains("haoyi"))
         assert(success2.text().contains("Test Message!"))
+        assert(success2.text().contains("2020-"))
         assert(success2.statusCode == 200)
       }
       withServer(MinimalApplication) { host =>
```
