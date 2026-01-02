           form(action := "/", method := "post")(
-            input(`type` := "text", name := "name", placeholder := "User name"),
-            input(`type` := "text", name := "msg", placeholder := "Write a message!"),
+            input(
+              `type` := "text",
+              name := "name",
+              placeholder := "User name",
+              userName.map(value := _)
+            ),
+            input(
+              `type` := "text",
+              name := "msg",
+              placeholder := "Write a message!",
+              msg.map(value := _)
+            ),
             input(`type` := "submit")