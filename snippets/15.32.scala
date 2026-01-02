   db.updateRaw("CREATE TABLE IF NOT EXISTS message (name text, msg text);")
+  def messages = db.run(Message.select.map(m => (m.name, m.msg)))

   var openConnections = Set.empty[cask.WsChannelActor]