   def postChatMsg(name: String, msg: String) =
     if name == "" then
-      hello(Some("Name cannot be empty"))
+      hello(Some("Name cannot be empty"), Some(name), Some(msg))
     else if msg == "" then
-      hello(Some("Message cannot be empty"))
+      hello(Some("Message cannot be empty"), Some(name), Some(msg))
     else
       messages = messages :+ (name -> msg)
-      hello()
+      hello(None, Some(name), None)