-         for (error <- errorOpt) yield i(color.red)(error),
+         div(id := "errorDiv", color.red),
          form(action := "/", method := "post")(
-            input(
-              `type` := "text",
-              name := "name",
-              placeholder := "User name",
-              userName.map(value := _)
-            ),
+           input(`type` := "text", id := "nameInput", placeholder := "User name"),
-            input(
-              `type` := "text",
-              name := "msg",
-              placeholder := "Write a message!",
-              msg.map(value := _)
-            ),
+           input(`type` := "text", id := "msgInput", placeholder := "Write a message!"),
            input(`type` := "submit")
          )