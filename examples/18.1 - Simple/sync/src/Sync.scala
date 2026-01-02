package sync
object Sync:
  def main(src0: String, dest0: String): Unit =
    val (src, dest) = (os.Path(src0, os.pwd), os.Path(dest0, os.pwd))
    val agentExecutable = os.temp(os.read.bytes(os.resource / "agent.jar"))
    os.perms.set(agentExecutable, "rwx------")
    val agent = os.spawn(cmd = agentExecutable, cwd = dest)

    enum Msg:
      case ChangedPath(value: os.SubPath)
      case AgentResponse(value: Rpc.StatInfo)

    import castor.Context.Simple.global
    object SyncActor extends castor.SimpleActor[Msg]:
      def run(msg: Msg): Unit = msg match
        case Msg.ChangedPath(value) => Shared.send(agent.stdin.data, Rpc.StatPath(value))
        case Msg.AgentResponse(Rpc.StatInfo(p, remoteHash)) =>
          val localHash = Shared.hashPath(src / p)
          if localHash != remoteHash && localHash.isDefined then
            Shared.send(agent.stdin.data, Rpc.WriteOver(os.read.bytes(src / p), p))

    val agentReader = Thread(() =>
      while agent.isAlive() do
        SyncActor.send(Msg.AgentResponse(Shared.receive[Rpc.StatInfo](agent.stdout.data)))
    )
    agentReader.start()

    val watcher = os.watch.watch(
      Seq(src),
      onEvent = _.foreach(p => SyncActor.send(Msg.ChangedPath(p.subRelativeTo(src))))
    )
    Thread.sleep(Long.MaxValue)
