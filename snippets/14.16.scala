   def postChatMsg(name: String, msg: String) =
-    if name != "" && msg != "" then messages = messages :+ (name -> msg)
-    hello()
+    if name == "" then
+      hello(Some("Name cannot be empty"))
+    else if msg == "" then
+      hello(Some("Message cannot be empty"))
+    else
+      messages = messages :+ (name -> msg)
+      hello()