# Example 14.6 - WebsocketsSynchronized
Websocket-based chat website with shared mutable state properly synchronized

```bash
./mill -i app.test
```

## Upstream Example: [14.4 - Websockets](https://github.com/handsonscala/handsonscala/tree/v1/examples/14.4%20-%20Websockets):
Diff:
```diff
diff --git a/14.4 - Websockets/app/src/MinimalApplication.scala b/14.6 - WebsocketsSynchronized/app/src/MinimalApplication.scala
index d5614ab..838aad4 100644
--- a/14.4 - Websockets/app/src/MinimalApplication.scala	
+++ b/14.6 - WebsocketsSynchronized/app/src/MinimalApplication.scala	
@@ -30,15 +30,20 @@ object MinimalApplication extends cask.MainRoutes {
     )
   )
 
-  def messageList() = frag(for ((name, msg) <- messages) yield p(b(name), " ", msg))
+  def messageList() = frag(
+    for ((name, msg) <- synchronized(messages))
+    yield p(b(name), " ", msg)
+  )
 
   @cask.postJson("/")
   def postChatMsg(name: String, msg: String) = {
     if (name == "") ujson.Obj("success" -> false, "err" -> "Name cannot be empty")
     else if (msg == "") ujson.Obj("success" -> false, "err" -> "Message cannot be empty")
-    else {
-      messages = messages :+ (name -> msg)
-      for (conn <- openConnections) conn.send(cask.Ws.Text(messageList().render))
+    else synchronized{
+      synchronized{ messages = messages :+ (name -> msg) }
+      for (conn <- synchronized(openConnections)) {
+        conn.send(cask.Ws.Text(messageList().render))
+      }
       ujson.Obj("success" -> true, "err" -> "")
     }
   }
@@ -46,8 +51,10 @@ object MinimalApplication extends cask.MainRoutes {
   @cask.websocket("/subscribe")
   def subscribe() = cask.WsHandler { connection =>
     connection.send(cask.Ws.Text(messageList().render))
-    openConnections += connection
-    cask.WsActor { case cask.Ws.Close(_, _) => openConnections -= connection }
+    synchronized{ openConnections += connection }
+    cask.WsActor { case cask.Ws.Close(_, _) =>
+      synchronized {openConnections -= connection }
+    }
   }
 
   initialize()
diff --git a/14.4 - Websockets/app/test/src/ExampleTests.scala b/14.6 - WebsocketsSynchronized/app/test/src/ExampleTests.scala
index 15c1c74..fdc33c6 100644
--- a/14.4 - Websockets/app/test/src/ExampleTests.scala	
+++ b/14.6 - WebsocketsSynchronized/app/test/src/ExampleTests.scala	
@@ -7,12 +7,12 @@ import castor.Context.Simple.global, cask.util.Logger.Console._
 object ExampleTests extends TestSuite {
   def withServer[T](example: cask.main.Main)(f: String => T): T = {
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
   }
```
