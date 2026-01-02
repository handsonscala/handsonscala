     else
-      messages = messages :+ (name -> msg)
+      db.run(Message.insert.values(Message(name, msg)))
       for (conn <- openConnections) conn.send(cask.Ws.Text(messageList().render))