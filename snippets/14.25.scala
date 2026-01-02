-  @cask.postForm("/")
+  @cask.postJson("/")
   def postChatMsg(name: String, msg: String) =
     if name == "" then
-      hello(Some("Name cannot be empty"), Some(name), Some(msg))
+      ujson.Obj("success" -> false, "err" -> "Name cannot be empty")
     else if msg == "" then
-      hello(Some("Message cannot be empty"), Some(name), Some(msg))
+      ujson.Obj("success" -> false, "err" -> "Message cannot be empty")
     else
       messages = messages :+ (name -> msg)
-      hello(None, Some(name), None)
+      ujson.Obj("success" -> true, "txt" -> messageList().render, "err" -> "")