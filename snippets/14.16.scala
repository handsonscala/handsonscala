   def postChatMsg(name: String, msg: String) = {
-    if (name != "" && msg != "") messages = messages :+ (name -> msg)
-    hello()
+    if (name == "") hello(Some("Name cannot be empty"))
+    else if (msg == "") hello(Some("Message cannot be empty"))
+    else {
+      messages = messages :+ (name -> msg)
+      hello()
+    }
   }