# Example 15.5 - ThreadedChat
Database-backed websocket threaded-chat website

```bash
./mill -i app.test
```

## Upstream Example: [14.4 - Websockets](https://github.com/handsonscala/handsonscala/tree/v1/examples/14.4%20-%20Websockets):
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
index d5614ab..58eecb2 100644
--- a/14.4 - Websockets/app/src/MinimalApplication.scala	
+++ b/15.5 - ThreadedChat/app/src/MinimalApplication.scala	
@@ -1,7 +1,26 @@
 package app
 import scalatags.Text.all._
 object MinimalApplication extends cask.MainRoutes {
-  var messages = Vector(("alice", "Hello World!"), ("bob", "I am cow, hear me moo"))
+  case class Message(id: Int, parent: Option[Int], name: String, msg: String)
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
+  ctx.executeAction(
+    "CREATE TABLE IF NOT EXISTS message (id serial, parent integer, name text, msg text);"
+  )
+  import ctx._
+
+  def messages = ctx.run(query[Message])
+
   var openConnections = Set.empty[cask.WsChannelActor]
   val bootstrap = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
 
@@ -21,6 +40,7 @@ object MinimalApplication extends cask.MainRoutes {
           div(id := "messageList")(messageList()),
           div(id := "errorDiv", color.red),
           form(onsubmit := "submitForm(); return false")(
+            input(`type` := "text", id := "parentInput", placeholder := "Reply To (Optional)"),
             input(`type` := "text", id := "nameInput", placeholder := "User name"),
             input(`type` := "text", id := "msgInput", placeholder := "Write a message!"),
             input(`type` := "submit")
@@ -30,14 +50,28 @@ object MinimalApplication extends cask.MainRoutes {
     )
   )
 
-  def messageList() = frag(for ((name, msg) <- messages) yield p(b(name), " ", msg))
+  def messageList(): Frag = {
+    val msgMap = messages.groupBy(_.parent)
+    def messageListFrag(parent: Option[Int] = None): Frag = frag(
+      for (msg <- msgMap.getOrElse(parent, Nil)) yield div(
+        p("#", msg.id, " ", b(msg.name), " ", msg.msg),
+        div(paddingLeft := 25)(messageListFrag(Some(msg.id)))
+      )
+    )
+    messageListFrag(None)
+  }
 
   @cask.postJson("/")
-  def postChatMsg(name: String, msg: String) = {
+  def postChatMsg(parent: String, name: String, msg: String) = {
     if (name == "") ujson.Obj("success" -> false, "err" -> "Name cannot be empty")
     else if (msg == "") ujson.Obj("success" -> false, "err" -> "Message cannot be empty")
     else {
-      messages = messages :+ (name -> msg)
+      val parentInt = parent match{case "" => None case n => Some(n.toInt)}
+      ctx.run(
+        query[Message].insert(
+          _.parent -> lift(parentInt), _.name -> lift(name), _.msg -> lift(msg)
+        )
+      )
       for (conn <- openConnections) conn.send(cask.Ws.Text(messageList().render))
       ujson.Obj("success" -> true, "err" -> "")
     }
diff --git a/14.4 - Websockets/app/test/src/ExampleTests.scala b/15.5 - ThreadedChat/app/test/src/ExampleTests.scala
index f0328d2..67d3190 100644
--- a/14.4 - Websockets/app/test/src/ExampleTests.scala	
+++ b/15.5 - ThreadedChat/app/test/src/ExampleTests.scala	
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
@@ -26,21 +27,12 @@ object ExampleTests extends TestSuite {
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
@@ -48,41 +40,31 @@ object ExampleTests extends TestSuite {
 
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
-      val response1 = requests.post(host, data = ujson.Obj("name" -> "haoyi"), check = false)
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
   }
 }
diff --git a/14.4 - Websockets/build.sc b/15.5 - ThreadedChat/build.sc
index 55bec98..540907e 100644
--- a/14.4 - Websockets/build.sc	
+++ b/15.5 - ThreadedChat/build.sc	
@@ -3,6 +3,9 @@ import mill._, scalalib._
 object app extends ScalaModule {
   def scalaVersion = "2.13.2"
   def ivyDeps = Agg(
+    ivy"io.getquill::quill-jdbc:3.5.2",
+    ivy"org.postgresql:postgresql:42.2.8",
+    ivy"com.opentable.components:otj-pg-embedded:0.13.1",
     ivy"com.lihaoyi::scalatags:0.9.1",
     ivy"com.lihaoyi::cask:0.7.4"
   )
```
