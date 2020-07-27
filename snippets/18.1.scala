object SyncActor extends castor.SimpleActor[Msg]{
  def run(msg: Msg): Unit = msg match {
    case ChangedPath(value) => Shared.send(agent.stdin.data, Rpc.StatPath(value))
    case AgentResponse(Rpc.StatInfo(p, remoteHash)) =>
      val localHash = Shared.hashPath(src / p)
      if (localHash != remoteHash && localHash.isDefined) {
        Shared.send(agent.stdin.data, Rpc.WriteOver(os.read.bytes(src / p), p))
      }
  }
}
