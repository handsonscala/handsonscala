     else {
-      messages = messages :+ (name -> msg)
+      ctx.run(query[Message].insert(lift(Message(name, msg))))
       for (conn <- openConnections) conn.send(cask.Ws.Text(messageList().render))