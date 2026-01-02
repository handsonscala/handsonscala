# Example 14.6 - WebsocketsSynchronized
Websocket-based chat website with shared mutable state properly synchronized

```bash
./mill -i app.test
```

## Upstream Example: [14.4 - Websockets](https://github.com/handsonscala/handsonscala/tree/v2/examples/14.4%20-%20Websockets):
Diff:
```diff
diff --git a/14.4 - Websockets/app/src/MinimalApplication.scala b/14.6 - WebsocketsSynchronized/app/src/MinimalApplication.scala
index 63d571a..c981859 100644
--- a/14.4 - Websockets/app/src/MinimalApplication.scala	
+++ b/14.6 - WebsocketsSynchronized/app/src/MinimalApplication.scala	
@@ -1,6 +1,7 @@
 package app
 import scalatags.Text.all.*
 object MinimalApplication extends cask.MainRoutes:
+  self =>
   var messages = Vector(("alice", "Hello World!"), ("bob", "I am cow, hear me moo"))
   var openConnections = Set.empty[cask.WsChannelActor]
   val bootstrap = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
@@ -30,7 +31,10 @@ object MinimalApplication extends cask.MainRoutes:
     )
   )
 
-  def messageList() = frag(for (name, msg) <- messages yield p(b(name), " ", msg))
+  def messageList() = frag(
+    for (name, msg) <- synchronized { messages }
+    yield p(b(name), " ", msg)
+  )
 
   @cask.postJson("/")
   def postChatMsg(name: String, msg: String) =
@@ -38,16 +42,19 @@ object MinimalApplication extends cask.MainRoutes:
       ujson.Obj("success" -> false, "err" -> "Name cannot be empty")
     else if msg == "" then
       ujson.Obj("success" -> false, "err" -> "Message cannot be empty")
-    else
-      messages = messages :+ (name -> msg)
-      for conn <- openConnections do conn.send(cask.Ws.Text(messageList().render))
+    else synchronized {
+      synchronized{ messages = messages :+ (name -> msg) }
+      for conn <- synchronized { openConnections } do
+        conn.send(cask.Ws.Text(messageList().render))
       ujson.Obj("success" -> true, "err" -> "")
+    }
 
   @cask.websocket("/subscribe")
   def subscribe() = cask.WsHandler: connection =>
     connection.send(cask.Ws.Text(messageList().render))
-    openConnections += connection
+    self.synchronized { openConnections += connection }
     cask.WsActor:
-      case cask.Ws.Close(_, _) => openConnections -= connection
+      case cask.Ws.Close(_, _) =>
+        self.synchronized { openConnections -= connection }
 
   initialize()
diff --git a/14.4 - Websockets/app/test/src/ExampleTests.scala b/14.6 - WebsocketsSynchronized/app/test/src/ExampleTests.scala
index 1ca626d..0303eb2 100644
--- a/14.4 - Websockets/app/test/src/ExampleTests.scala	
+++ b/14.6 - WebsocketsSynchronized/app/test/src/ExampleTests.scala	
@@ -7,12 +7,12 @@ import castor.Context.Simple.global, cask.util.Logger.Console._
 object ExampleTests extends TestSuite:
   def withServer[T](example: cask.main.Main)(f: String => T): T =
     val server = io.undertow.Undertow.builder
-      .addHttpListener(8084, "localhost")
+      .addHttpListener(8086, "localhost")
       .setHandler(example.defaultHandler)
       .build
     server.start()
     val res =
-      try f("http://localhost:8084")
+      try f("http://localhost:8086")
       finally server.stop()
     res
 
```
