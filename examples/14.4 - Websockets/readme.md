# Example 14.4 - Websockets
Websocket-based real-time chat website

```bash
./mill -i app.test
```

## Upstream Example: [14.3 - Ajax](https://github.com/handsonscala/handsonscala/tree/v1/examples/14.3%20-%20Ajax):
Diff:
```diff
diff --git a/14.3 - Ajax/app/resources/static/app.js b/14.4 - Websockets/app/resources/static/app.js
index 333d200..efb34ca 100644
--- a/14.3 - Ajax/app/resources/static/app.js	
+++ b/14.4 - Websockets/app/resources/static/app.js	
@@ -4,10 +4,10 @@ function submitForm() {
     {method: "POST", body: JSON.stringify({name: nameInput.value, msg: msgInput.value})}
   ).then(response => response.json())
    .then(json => {
-    if (json["success"]) {
-      messageList.innerHTML = json["txt"]
-      msgInput.value = ""
-    }
+    if (json["success"]) msgInput.value = ""
     errorDiv.innerText = json["err"]
   })
 }
+
+var socket = new WebSocket("ws://" + location.host + "/subscribe");
+socket.onmessage = function (ev) { messageList.innerHTML = ev.data }
\ No newline at end of file
diff --git a/14.3 - Ajax/app/src/MinimalApplication.scala b/14.4 - Websockets/app/src/MinimalApplication.scala
index 01e7963..d5614ab 100644
--- a/14.3 - Ajax/app/src/MinimalApplication.scala	
+++ b/14.4 - Websockets/app/src/MinimalApplication.scala	
@@ -2,6 +2,7 @@ package app
 import scalatags.Text.all._
 object MinimalApplication extends cask.MainRoutes {
   var messages = Vector(("alice", "Hello World!"), ("bob", "I am cow, hear me moo"))
+  var openConnections = Set.empty[cask.WsChannelActor]
   val bootstrap = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
 
   @cask.staticResources("/static")
@@ -37,9 +38,17 @@ object MinimalApplication extends cask.MainRoutes {
     else if (msg == "") ujson.Obj("success" -> false, "err" -> "Message cannot be empty")
     else {
       messages = messages :+ (name -> msg)
-      ujson.Obj("success" -> true, "txt" -> messageList().render, "err" -> "")
+      for (conn <- openConnections) conn.send(cask.Ws.Text(messageList().render))
+      ujson.Obj("success" -> true, "err" -> "")
     }
   }
 
+  @cask.websocket("/subscribe")
+  def subscribe() = cask.WsHandler { connection =>
+    connection.send(cask.Ws.Text(messageList().render))
+    openConnections += connection
+    cask.WsActor { case cask.Ws.Close(_, _) => openConnections -= connection }
+  }
+
   initialize()
 }
diff --git a/14.3 - Ajax/app/test/src/ExampleTests.scala b/14.4 - Websockets/app/test/src/ExampleTests.scala
index 112a9f6..15c1c74 100644
--- a/14.3 - Ajax/app/test/src/ExampleTests.scala	
+++ b/14.4 - Websockets/app/test/src/ExampleTests.scala	
@@ -1,22 +1,28 @@
 package app
 
 import utest._
+import scala.concurrent._, duration.Duration.Inf
+import castor.Context.Simple.global, cask.util.Logger.Console._
 
 object ExampleTests extends TestSuite {
   def withServer[T](example: cask.main.Main)(f: String => T): T = {
     val server = io.undertow.Undertow.builder
-      .addHttpListener(8083, "localhost")
+      .addHttpListener(8084, "localhost")
       .setHandler(example.defaultHandler)
       .build
     server.start()
     val res =
-      try f("http://localhost:8083")
+      try f("http://localhost:8084")
       finally server.stop()
     res
   }
 
   val tests = Tests {
     test("success") - withServer(MinimalApplication) { host =>
+      var wsPromise = scala.concurrent.Promise[String]
+      val wsClient = cask.util.WsClient.connect(s"$host/subscribe") {
+        case cask.Ws.Text(msg) => wsPromise.success(msg)
+      }
       val success = requests.get(host)
 
       assert(success.text().contains("Scala Chat!"))
@@ -26,20 +32,39 @@ object ExampleTests extends TestSuite {
       assert(success.text().contains("I am cow, hear me moo"))
       assert(success.statusCode == 200)
 
+      val wsMsg = Await.result(wsPromise.future, Inf)
+
+      assert(wsMsg.contains("alice"))
+      assert(wsMsg.contains("Hello World!"))
+      assert(wsMsg.contains("bob"))
+      assert(wsMsg.contains("I am cow, hear me moo"))
+
+      wsPromise = scala.concurrent.Promise[String]
       val response = requests.post(host, data = ujson.Obj("name" -> "haoyi", "msg" -> "Test Message!"))
 
       val parsed = ujson.read(response.text())
       assert(parsed("success") == ujson.True)
       assert(parsed("err") == ujson.Str(""))
 
-      val parsedTxt = parsed("txt").str
-      assert(parsedTxt.contains("alice"))
-      assert(parsedTxt.contains("Hello World!"))
-      assert(parsedTxt.contains("bob"))
-      assert(parsedTxt.contains("I am cow, hear me moo"))
-      assert(parsedTxt.contains("haoyi"))
-      assert(parsedTxt.contains("Test Message!"))
       assert(response.statusCode == 200)
+      val wsMsg2 = Await.result(wsPromise.future, Inf)
+      assert(wsMsg2.contains("alice"))
+      assert(wsMsg2.contains("Hello World!"))
+      assert(wsMsg2.contains("bob"))
+      assert(wsMsg2.contains("I am cow, hear me moo"))
+      assert(wsMsg2.contains("haoyi"))
+      assert(wsMsg2.contains("Test Message!"))
+
+      val success2 = requests.get(host)
+
+      assert(success2.text().contains("Scala Chat!"))
+      assert(success2.text().contains("alice"))
+      assert(success2.text().contains("Hello World!"))
+      assert(success2.text().contains("bob"))
+      assert(success2.text().contains("I am cow, hear me moo"))
+      assert(success2.text().contains("haoyi"))
+      assert(success2.text().contains("Test Message!"))
+      assert(success2.statusCode == 200)
     }
     test("failure") - withServer(MinimalApplication) { host =>
       val response1 = requests.post(host, data = ujson.Obj("name" -> "haoyi"), check = false)
```
## Downstream Examples

- [14.5 - WebsocketsFilter](https://github.com/handsonscala/handsonscala/tree/v1/examples/14.5%20-%20WebsocketsFilter)
- [14.6 - WebsocketsSynchronized](https://github.com/handsonscala/handsonscala/tree/v1/examples/14.6%20-%20WebsocketsSynchronized)
- [14.7 - WebsocketsJsoup](https://github.com/handsonscala/handsonscala/tree/v1/examples/14.7%20-%20WebsocketsJsoup)
- [15.2 - Website](https://github.com/handsonscala/handsonscala/tree/v1/examples/15.2%20-%20Website)