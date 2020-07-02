-          div(
-            input(`type` := "text", placeholder := "User name"),
-            input(`type` := "text", placeholder := "Write a message!", width := "100%")
+          form(action := "/", method := "post")(
+            input(`type` := "text", name := "name", placeholder := "User name"),
+            input(`type` := "text", name := "msg", placeholder := "Write a message!"),
+            input(`type` := "submit")
           )