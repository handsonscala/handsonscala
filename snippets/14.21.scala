   def postChatMsg(name: String, msg: String) = {
-    if (name == "") hello(Some("Name cannot be empty"))
-    else if (msg == "") hello(Some("Message cannot be empty"))
+    if (name == "") hello(Some("Name cannot be empty"), Some(name), Some(msg))
+    else if (msg == "") hello(Some("Message cannot be empty"), Some(name), Some(msg))
     else {
       messages = messages :+ (name -> msg)
-      hello()
+      hello(None, Some(name), None)
     }