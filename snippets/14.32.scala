+ @cask.websocket("/subscribe")
+ def subscribe() = cask.WsHandler { connection =>
+   connection.send(cask.Ws.Text(messageList().render))
+   openConnections += connection
+   cask.WsActor { case cask.Ws.Close(_, _) => openConnections -= connection }
+ }