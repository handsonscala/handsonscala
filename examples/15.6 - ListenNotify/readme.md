# Example 15.6 - ListenNotify
Database-backed websocket chat website that uses the Postgres `LISTEN`/`NOTIFY`
feature to propagate push notifications across all connected webservers

```bash
./mill -i app.test
```

## Upstream Example: [15.2 - Website](https://github.com/handsonscala/handsonscala/tree/v2/examples/15.2%20-%20Website):
Diff:
```diff
diff --git a/15.2 - Website/app/src/MinimalApplication.scala b/15.6 - ListenNotify/app/src/MinimalApplication.scala
index be9cfeb..86afec6 100644
--- a/15.2 - Website/app/src/MinimalApplication.scala	
+++ b/15.6 - ListenNotify/app/src/MinimalApplication.scala	
@@ -1,4 +1,5 @@
 package app
+import com.impossibl.postgres.api.jdbc.{PGConnection, PGNotificationListener}
 import scalatags.Text.all.*
 import scalasql.simple.*, PostgresDialect.*
 object MinimalApplication extends cask.MainRoutes:
@@ -23,11 +24,23 @@ object MinimalApplication extends cask.MainRoutes:
   )
 
   val db = client.getAutoCommitClientConnection
-  sys.addShutdownHook{ db.close() }
+  sys.addShutdownHook{db.close()}
   db.updateRaw(
     """DROP TABLE IF EXISTS message;
     CREATE TABLE IF NOT EXISTS message (name text, msg text);""")
 
+  val asyncPgDataSource = com.impossibl.postgres.jdbc.PGDataSource()
+  asyncPgDataSource.setUser("postgres")
+  val connection = asyncPgDataSource.getConnection.unwrap(classOf[PGConnection])
+  connection.addNotificationListener(new PGNotificationListener() {
+    override def notification(processId: Int, channelName: String, payload: String) =
+      for conn <- openConnections do conn.send(cask.Ws.Text(messageList().render))
+  })
+
+  val listenStmt = connection.createStatement
+  listenStmt.executeUpdate("LISTEN msgs")
+  listenStmt.close()
+
   def messages = db.run(Message.select.map(m => (m.name, m.msg)))
 
   var openConnections = Set.empty[cask.WsChannelActor]
@@ -58,17 +71,17 @@ object MinimalApplication extends cask.MainRoutes:
     )
   )
 
-  def messageList() = frag(for ((name, msg) <- messages) yield p(b(name), " ", msg))
+  def messageList() = frag(for (name, msg) <- messages yield p(b(name), " ", msg))
 
   @cask.postJson("/")
   def postChatMsg(name: String, msg: String) =
-    if name == "" then
-      ujson.Obj("success" -> false, "err" -> "Name cannot be empty")
-    else if msg == "" then
-      ujson.Obj("success" -> false, "err" -> "Message cannot be empty")
+    if name == "" then ujson.Obj("success" -> false, "err" -> "Name cannot be empty")
+    else if msg == "" then ujson.Obj("success" -> false, "err" -> "Message cannot be empty")
     else
       db.run(Message.insert.values(Message(name, msg)))
-      for conn <- openConnections do conn.send(cask.Ws.Text(messageList().render))
+      val notifyStmt = connection.createStatement
+      notifyStmt.executeUpdate("NOTIFY msgs")
+      notifyStmt.close()
       ujson.Obj("success" -> true, "err" -> "")
 
   @cask.websocket("/subscribe")
diff --git a/15.2 - Website/app/test/src/ExampleTests.scala b/15.6 - ListenNotify/app/test/src/ExampleTests.scala
index 4140eed..d800d92 100644
--- a/15.2 - Website/app/test/src/ExampleTests.scala	
+++ b/15.6 - ListenNotify/app/test/src/ExampleTests.scala	
@@ -5,9 +5,9 @@ import scala.concurrent.*, duration.Duration.Inf
 import castor.Context.Simple.global, cask.util.Logger.Console.*
 
 object ExampleTests extends TestSuite:
-  def withServer[T](example: cask.main.Main)(f: String => T): T =
+  def withServer[T](example: cask.main.Main, ip: Int = 8087)(f: String => T): T =
     val server = io.undertow.Undertow.builder
-      .addHttpListener(8087, "localhost")
+      .addHttpListener(ip, "localhost")
       .setHandler(example.defaultHandler)
       .build
     server.start()
@@ -16,11 +16,14 @@ object ExampleTests extends TestSuite:
       finally server.stop()
     res
 
-  val tests = Tests {
+  val tests = Tests:
     test("success") - {
-      withServer(MinimalApplication) { host =>
+      withServer(MinimalApplication, ip = 8087) { host =>
+        withServer(MinimalApplication, ip = 8088) { host2 =>
           var wsPromise = scala.concurrent.Promise[String]
-        val wsHost = host.replace("http", "ws")
+          // Subscribe to websockets on `host2` while sending POST requests to `host`,
+          // to ensure that propagating notifications across multiple servers works
+          val wsHost = host2.replace("http", "ws")
           val wsClient = cask.util.WsClient.connect(s"$wsHost/subscribe"):
             case cask.Ws.Text(msg) => wsPromise.success(msg)
           val success = requests.get(host)
@@ -49,6 +52,7 @@ object ExampleTests extends TestSuite:
           assert(success2.text().contains("Test Message!"))
           assert(success2.statusCode == 200)
         }
+      }
       withServer(MinimalApplication) { host =>
         val success = requests.get(host)
 
@@ -76,4 +80,3 @@ object ExampleTests extends TestSuite:
       val response1 = requests.get(host + "/static/app.js")
       assert(response1.text().contains("function submitForm()"))
     }
-  }
diff --git a/15.2 - Website/build.mill b/15.6 - ListenNotify/build.mill
index 987658a..1cc9ad0 100644
--- a/15.2 - Website/build.mill	
+++ b/15.6 - ListenNotify/build.mill	
@@ -4,6 +4,7 @@ import mill.*, scalalib.*
 object app extends ScalaModule:
   def scalaVersion = "3.8.0-RC4"
   def mvnDeps = Seq(
+    mvn"com.impossibl.pgjdbc-ng:pgjdbc-ng:0.8.4",
     mvn"com.lihaoyi::scalasql-simple::0.2.7",
     mvn"org.postgresql:postgresql:42.7.8",
     mvn"io.zonky.test:embedded-postgres:2.1.1",
```
