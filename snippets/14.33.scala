       messages = messages :+ (name -> msg)
-      ujson.Obj("success" -> true, "txt" -> messageList().render, "err" -> "")
+      for (conn <- openConnections) conn.send(cask.Ws.Text(messageList().render))
+      ujson.Obj("success" -> true, "err" -> "")