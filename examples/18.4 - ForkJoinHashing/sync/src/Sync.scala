package sync
import scala.concurrent.Future
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
          if localHash != remoteHash && localHash.isDefined then
            Shared.send(agent.stdin.data, Rpc.WriteOver(os.read.bytes(src / p), p))

    enum HashActorMsg:
      case SingleStatInfo(value: Rpc.StatInfo)
      case HashComplete(values: Seq[Msg.HashStatInfo])

    object HashActor extends castor.StateMachineActor[HashActorMsg]:
      def initialState = Idle()
      case class Buffering(msgs: Map[os.SubPath, Option[Int]]) extends State({
        case HashActorMsg.SingleStatInfo(value) => Buffering(msgs + (value.p -> value.fileHash))
        case HashActorMsg.HashComplete(values) =>
          values.foreach(SyncActor.send(_))
          if msgs.isEmpty then Idle()
          else processBuffered(msgs)
      })

      case class Idle() extends State({
        case HashActorMsg.SingleStatInfo(statInfo) => processBuffered(Map(statInfo.p -> statInfo.fileHash))
      })

      def processBuffered(msgs: Map[os.SubPath, Option[Int]]) =
        val futures = for (p, fileHash) <- msgs yield Future[Msg.HashStatInfo]:
          Msg.HashStatInfo(Shared.hashPath(src / p), Rpc.StatInfo(p, fileHash))

        this.sendAsync(Future.sequence(futures.toSeq).map(HashActorMsg.HashComplete(_)))
        Buffering(Map())

    val agentReader = Thread(() =>
      while agent.isAlive() do
        HashActor.send(HashActorMsg.SingleStatInfo(Shared.receive[Rpc.StatInfo](agent.stdout.data)))
    )
    agentReader.start()

    val watcher = os.watch.watch(
      Seq(src),
      onEvent = _.foreach(p => SyncActor.send(Msg.ChangedPath(p.subRelativeTo(src))))
    )
    Thread.sleep(Long.MaxValue)
