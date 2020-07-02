package sync
import scala.concurrent.Future
object Sync {
  def main(args: Array[String]): Unit = {
    val (src, dest) = (os.Path(args(0), os.pwd), os.Path(args(1), os.pwd))
    val agentExecutable = os.temp(os.read.bytes(os.resource / "agent.jar"))
    os.perms.set(agentExecutable, "rwx------")
    val agent = os.proc(agentExecutable).spawn(cwd = dest)
    sealed trait Msg
    case class ChangedPath(value: os.SubPath) extends Msg
    case class HashStatInfo(localHash: Option[Int], value: Rpc.StatInfo) extends Msg
    import castor.Context.Simple.global
    object SyncActor extends castor.SimpleActor[Msg]{
      def run(msg: Msg): Unit = {
        println("SyncActor handling: " + msg)
        msg match {
          case ChangedPath(value) => Shared.send(agent.stdin.data, Rpc.StatPath(value))
          case HashStatInfo(localHash, Rpc.StatInfo(p, remoteHash)) =>
            if (localHash != remoteHash && localHash.isDefined) {
              Shared.send(agent.stdin.data, Rpc.WriteOver(os.read.bytes(src / p), p))
            }
        }
      }
    }
    sealed trait HashActorMsg
    case class SingleStatInfo(value: Rpc.StatInfo) extends HashActorMsg
    case class HashComplete(values: Seq[HashStatInfo]) extends HashActorMsg
    object HashActor extends castor.StateMachineActor[HashActorMsg]{
      def initialState = Idle()
      case class Buffering(msgs: Map[os.SubPath, Option[Int]]) extends State({
        case SingleStatInfo(value) => Buffering(msgs + (value.p -> value.fileHash))
        case HashComplete(values) =>
          values.foreach(SyncActor.send(_))
          if (msgs.isEmpty) Idle()
          else processBuffered(msgs)
      })
      case class Idle() extends State({
        case SingleStatInfo(statInfo) => processBuffered(Map(statInfo.p -> statInfo.fileHash))
      })
      def processBuffered(msgs: Map[os.SubPath, Option[Int]]) = {
        val futures = for((p, fileHash) <- msgs) yield Future {
          HashStatInfo(Shared.hashPath(src / p), Rpc.StatInfo(p, fileHash))
        }

        this.sendAsync(Future.sequence(futures.toSeq).map(HashComplete(_)))
        Buffering(Map())
      }
    }
    val agentReader = new Thread(() => {
      while (agent.isAlive()) {
        HashActor.send(SingleStatInfo(Shared.receive[Rpc.StatInfo](agent.stdout.data)))
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
