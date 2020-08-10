package sync
object Sync {
  def main(args: Array[String]): Unit = {
    val (src, dest) = (os.Path(args(0), os.pwd), os.Path(args(1), os.pwd))
    val agentExecutable = os.temp(os.read.bytes(os.resource / "agent.jar"))
    os.perms.set(agentExecutable, "rwx------")
    val agent = os.proc(agentExecutable).spawn(cwd = dest)
    sealed trait Msg
    case class ChangedPath(value: os.SubPath) extends Msg
    case class AgentResponse(value: Rpc.StatInfo) extends Msg
    import castor.Context.Simple.global
    object SyncActor extends castor.SimpleActor[Msg]{
      def run(msg: Msg): Unit = {
        println("SyncActor handling: " + msg)
        msg match {
          case ChangedPath(value) => Shared.send(agent.stdin.data, Rpc.StatPath(value))
          case AgentResponse(Rpc.StatInfo(p, remoteHash)) =>
            val localHash = Shared.hashPath(src / p)
            if (localHash != remoteHash && localHash.isDefined) {
              Shared.send(agent.stdin.data, Rpc.WriteOver(os.read.bytes(src / p), p))
            }
        }
      }
    }
    val agentReader = new Thread(() => {
      while (agent.isAlive()) {
        SyncActor.send(AgentResponse(Shared.receive[Rpc.StatInfo](agent.stdout.data)))
      }
    })
    agentReader.start()
    val watcher = os.watch.watch(
      Seq(src),
      onEvent = _.foreach(p => SyncActor.send(ChangedPath(p.subRelativeTo(src))))
    )
    Thread.sleep(Long.MaxValue)
  }
}