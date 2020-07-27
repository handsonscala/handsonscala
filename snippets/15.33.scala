   import ctx._
+  def messages = ctx.run(query[Message].map(m => (m.name, m.msg)))

   var openConnections = Set.empty[cask.WsChannelActor]