-          div(for ((name, msg) <- messages) yield p(b(name), " ", msg))
+          div(id := "messageList")(messageList()),