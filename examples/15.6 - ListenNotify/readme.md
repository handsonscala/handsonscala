# Example 15.6 - ListenNotify
Database-backed websocket chat website that uses the Postgres `LISTEN`/`NOTIFY`
feature to propagate push notifications across all connected webservers

```bash
./mill -i app.test
```

## Upstream Example: [15.2 - Website](https://github.com/handsonscala/handsonscala/tree/v1/examples/15.2%20-%20Website):
Diff:
```diff
diff --git a/15.2 - Website/app/src/MinimalApplication.scala b/15.6 - ListenNotify/app/src/MinimalApplication.scala
index f7f07fd..3288c1b 100644
--- a/15.2 - Website/app/src/MinimalApplication.scala	
+++ b/15.6 - ListenNotify/app/src/MinimalApplication.scala	
@@ -1,12 +1,17 @@
 package app
+import com.impossibl.postgres.api.jdbc.{PGConnection, PGNotificationListener}
 import scalatags.Text.all._
 object MinimalApplication extends cask.MainRoutes {
   case class Message(name: String, msg: String)
   import com.opentable.db.postgres.embedded.EmbeddedPostgres
-  val server = EmbeddedPostgres.builder()
+
+  // Start the database on a best-effort basis, in case some other
+  // process is already running it
+  val server = try Some(EmbeddedPostgres.builder()
     .setDataDirectory(System.getProperty("user.home") + "/data")
     .setCleanDataDirectory(false).setPort(5432)
-    .start()
+    .start())
+  catch{ case e => None}
   import io.getquill._
   import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
   val pgDataSource = new org.postgresql.ds.PGSimpleDataSource()
@@ -17,6 +22,18 @@ object MinimalApplication extends cask.MainRoutes {
   ctx.executeAction("CREATE TABLE IF NOT EXISTS message (name text, msg text);")
   import ctx._
 
+  val asyncPgDataSource = new com.impossibl.postgres.jdbc.PGDataSource()
+  asyncPgDataSource.setUser("postgres")
+  val connection = asyncPgDataSource.getConnection.unwrap(classOf[PGConnection])
+  connection.addNotificationListener(new PGNotificationListener() {
+    override def notification(processId: Int, channelName: String, payload: String)  = {
+      for (conn <- openConnections) conn.send(cask.Ws.Text(messageList().render))
+    }
+  })
+  val listenStmt = connection.createStatement
+  listenStmt.executeUpdate("LISTEN msgs")
+  listenStmt.close()
+
   def messages = ctx.run(query[Message].map(m => (m.name, m.msg)))
 
   var openConnections = Set.empty[cask.WsChannelActor]
@@ -55,7 +72,9 @@ object MinimalApplication extends cask.MainRoutes {
     else if (msg == "") ujson.Obj("success" -> false, "err" -> "Message cannot be empty")
     else {
       ctx.run(query[Message].insert(lift(Message(name, msg))))
-      for (conn <- openConnections) conn.send(cask.Ws.Text(messageList().render))
+      val notifyStmt = connection.createStatement
+      notifyStmt.executeUpdate("NOTIFY msgs")
+      notifyStmt.close()
       ujson.Obj("success" -> true, "err" -> "")
     }
   }
diff --git a/15.2 - Website/app/test/src/ExampleTests.scala b/15.6 - ListenNotify/app/test/src/ExampleTests.scala
index 1e93cb2..5546381 100644
--- a/15.2 - Website/app/test/src/ExampleTests.scala	
+++ b/15.6 - ListenNotify/app/test/src/ExampleTests.scala	
@@ -5,9 +5,9 @@ import scala.concurrent._, duration.Duration.Inf
 import castor.Context.Simple.global, cask.util.Logger.Console._
 
 object ExampleTests extends TestSuite {
-  def withServer[T](example: cask.main.Main)(f: String => T): T = {
+  def withServer[T](example: cask.main.Main, ip: Int = 8087)(f: String => T): T = {
     val server = io.undertow.Undertow.builder
-      .addHttpListener(8087, "localhost")
+      .addHttpListener(ip, "localhost")
       .setHandler(example.defaultHandler)
       .build
     server.start()
@@ -19,9 +19,12 @@ object ExampleTests extends TestSuite {
 
   val tests = Tests {
     test("success") - {
-      withServer(MinimalApplication) { host =>
+      withServer(MinimalApplication, ip = 8087) { host =>
+        withServer(MinimalApplication, ip = 8088) { host2 =>
           var wsPromise = scala.concurrent.Promise[String]
-        val wsClient = cask.util.WsClient.connect(s"$host/subscribe") {
+          // Subscribe to websockets on `host2` while sending POST requests to `host`,
+          // to ensure that propagating notifications across multiple servers works
+          val wsClient = cask.util.WsClient.connect(s"$host2/subscribe") {
             case cask.Ws.Text(msg) => wsPromise.success(msg)
           }
           val success = requests.get(host)
@@ -50,6 +53,7 @@ object ExampleTests extends TestSuite {
           assert(success2.text().contains("Test Message!"))
           assert(success2.statusCode == 200)
         }
+      }
       withServer(MinimalApplication) { host =>
         val success = requests.get(host)
 
diff --git a/15.2 - Website/build.sc b/15.6 - ListenNotify/build.sc
index dd5f2f5..0447197 100644
--- a/15.2 - Website/build.sc	
+++ b/15.6 - ListenNotify/build.sc	
@@ -3,6 +3,7 @@ import mill._, scalalib._
 object app extends ScalaModule {
   def scalaVersion = "2.13.2"
   def ivyDeps = Agg(
+    ivy"com.impossibl.pgjdbc-ng:pgjdbc-ng:0.8.4",
     ivy"io.getquill::quill-jdbc:3.5.0",
     ivy"org.postgresql:postgresql:42.2.8",
     ivy"com.opentable.components:otj-pg-embedded:0.13.1",
```
