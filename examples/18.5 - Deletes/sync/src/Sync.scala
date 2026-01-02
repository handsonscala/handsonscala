package sync
object Sync:
  def main(src0: String, dest0: String): Unit =
    val (src, dest) = (os.Path(src0, os.pwd), os.Path(dest0, os.pwd))
    val agentExecutable = os.temp(os.read.bytes(os.resource / "agent.jar"))
    os.perms.set(agentExecutable, "rwx------")
    val agent = os.spawn(cmd = agentExecutable, cwd = dest)

    enum Msg:
      case ChangedPath(value: os.SubPath)
      case HashStatInfo(localHash: Option[Int], value: Rpc.StatInfo)

    import castor.Context.Simple.global
    object SyncActor extends castor.SimpleActor[Msg]:
      def run(msg: Msg): Unit = msg match
        case Msg.ChangedPath(value) => Shared.send(agent.stdin.data, Rpc.StatPath(value))
        case Msg.HashStatInfo(localHash, Rpc.StatInfo(p, remoteHash)) =>
          if localHash != remoteHash then
            if localHash.isDefined then
              Shared.send(agent.stdin.data, Rpc.WriteOver(os.read.bytes(src / p), p))
            else
              Shared.send(agent.stdin.data, Rpc.Delete(p))

    object HashActor extends castor.SimpleActor[Rpc.StatInfo]:
      def run(msg: Rpc.StatInfo): Unit =
        val localHash = Shared.hashPath(src / msg.p)
        SyncActor.send(Msg.HashStatInfo(localHash, msg))

    val agentReader = Thread(() =>
      while agent.isAlive() do
        HashActor.send(Shared.receive[Rpc.StatInfo](agent.stdout.data))
    )
    agentReader.start()

    val watcher = os.watch.watch(
      Seq(src),
      onEvent = _.foreach(p => SyncActor.send(Msg.ChangedPath(p.subRelativeTo(src))))
    )
    Thread.sleep(Long.MaxValue)
