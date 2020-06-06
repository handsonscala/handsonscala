# Example 14.5 - WebsocketsFilter
Websocket-based chat website with ability to filter chats

```bash
./mill -i app.test
```

## Upstream Example: [14.4 - Websockets](https://github.com/handsonscala/handsonscala/tree/v1/examples/14.4%20-%20Websockets):
Diff:
```diff
diff --git a/14.4 - Websockets/app/src/MinimalApplication.scala b/14.5 - WebsocketsFilter/app/src/MinimalApplication.scala
index d5614ab..9c47953 100644
--- a/14.4 - Websockets/app/src/MinimalApplication.scala	
+++ b/14.5 - WebsocketsFilter/app/src/MinimalApplication.scala	
@@ -2,14 +2,14 @@ package app
 import scalatags.Text.all._
 object MinimalApplication extends cask.MainRoutes {
   var messages = Vector(("alice", "Hello World!"), ("bob", "I am cow, hear me moo"))
-  var openConnections = Set.empty[cask.WsChannelActor]
+  var openConnections = Map.empty[cask.WsChannelActor, String]
   val bootstrap = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
 
   @cask.staticResources("/static")
   def staticResourceRoutes() = "static"
 
   @cask.get("/")
-  def hello() = doctype("html")(
+  def hello(filter: String = "") = doctype("html")(
     html(
       head(
         link(rel := "stylesheet", href := bootstrap),
@@ -18,19 +18,32 @@ object MinimalApplication extends cask.MainRoutes {
       body(
         div(cls := "container")(
           h1("Scala Chat!"),
-          div(id := "messageList")(messageList()),
+          div(id := "messageList")(messageList(filter)),
           div(id := "errorDiv", color.red),
           form(onsubmit := "submitForm(); return false")(
             input(`type` := "text", id := "nameInput", placeholder := "User name"),
             input(`type` := "text", id := "msgInput", placeholder := "Write a message!"),
             input(`type` := "submit")
+          ),
+          form(action := "/", method := "get")(
+            input(
+              `type` := "text",
+              name := "filter",
+              placeholder := "Filter Messages",
+              value := filter
+            ),
+            input(`type` := "submit")
           )
+
         )
       )
     )
   )
 
-  def messageList() = frag(for ((name, msg) <- messages) yield p(b(name), " ", msg))
+  def messageList(filter: String) = frag(
+    for ((name, msg) <- messages if filter == "" || name == filter)
+    yield p(b(name), " ", msg)
+  )
 
   @cask.postJson("/")
   def postChatMsg(name: String, msg: String) = {
@@ -38,16 +51,26 @@ object MinimalApplication extends cask.MainRoutes {
     else if (msg == "") ujson.Obj("success" -> false, "err" -> "Message cannot be empty")
     else {
       messages = messages :+ (name -> msg)
-      for (conn <- openConnections) conn.send(cask.Ws.Text(messageList().render))
+      for ((conn, filter) <- openConnections) {
+        conn.send(cask.Ws.Text(messageList(filter).render))
+      }
       ujson.Obj("success" -> true, "err" -> "")
     }
   }
 
   @cask.websocket("/subscribe")
   def subscribe() = cask.WsHandler { connection =>
-    connection.send(cask.Ws.Text(messageList().render))
-    openConnections += connection
-    cask.WsActor { case cask.Ws.Close(_, _) => openConnections -= connection }
+    println("Subscribe")
+
+    cask.WsActor {
+      case cask.Ws.Text(filter) =>
+        println("Text")
+        openConnections += (connection -> filter)
+        connection.send(cask.Ws.Text(messageList(filter).render))
+      case cask.Ws.Close(_, _) =>
+        println("Close")
+        openConnections -= connection
+    }
   }
 
   initialize()
diff --git a/14.4 - Websockets/app/test/src/ExampleTests.scala b/14.5 - WebsocketsFilter/app/test/src/ExampleTests.scala
index 15c1c74..2978d6a 100644
--- a/14.4 - Websockets/app/test/src/ExampleTests.scala	
+++ b/14.5 - WebsocketsFilter/app/test/src/ExampleTests.scala	
@@ -7,12 +7,12 @@ import castor.Context.Simple.global, cask.util.Logger.Console._
 object ExampleTests extends TestSuite {
   def withServer[T](example: cask.main.Main)(f: String => T): T = {
     val server = io.undertow.Undertow.builder
-      .addHttpListener(8084, "localhost")
+      .addHttpListener(8085, "localhost")
       .setHandler(example.defaultHandler)
       .build
     server.start()
     val res =
-      try f("http://localhost:8084")
+      try f("http://localhost:8085")
       finally server.stop()
     res
   }
@@ -23,6 +23,7 @@ object ExampleTests extends TestSuite {
       val wsClient = cask.util.WsClient.connect(s"$host/subscribe") {
         case cask.Ws.Text(msg) => wsPromise.success(msg)
       }
+      wsClient.send(cask.Ws.Text(""))
       val success = requests.get(host)
 
       assert(success.text().contains("Scala Chat!"))
@@ -40,7 +41,10 @@ object ExampleTests extends TestSuite {
       assert(wsMsg.contains("I am cow, hear me moo"))
 
       wsPromise = scala.concurrent.Promise[String]
-      val response = requests.post(host, data = ujson.Obj("name" -> "haoyi", "msg" -> "Test Message!"))
+      val response = requests.post(host, data = ujson.Obj(
+        "name" -> "haoyi",
+        "msg" -> "Test Message!",
+      ))
 
       val parsed = ujson.read(response.text())
       assert(parsed("success") == ujson.True)
@@ -65,6 +69,17 @@ object ExampleTests extends TestSuite {
       assert(success2.text().contains("haoyi"))
       assert(success2.text().contains("Test Message!"))
       assert(success2.statusCode == 200)
+
+      val filtered = requests.get(host, params = Map("filter" -> "bob"))
+
+      assert(filtered.text().contains("Scala Chat!"))
+      assert(!filtered.text().contains("alice"))
+      assert(!filtered.text().contains("Hello World!"))
+      assert(filtered.text().contains("bob"))
+      assert(filtered.text().contains("I am cow, hear me moo"))
+      assert(!filtered.text().contains("haoyi"))
+      assert(!filtered.text().contains("Test Message!"))
+      assert(filtered.statusCode == 200)
     }
     test("failure") - withServer(MinimalApplication) { host =>
       val response1 = requests.post(host, data = ujson.Obj("name" -> "haoyi"), check = false)
```
