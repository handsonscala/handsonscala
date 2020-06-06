package sync
object Sync {
  def main(args: Array[String]): Unit = {
    val (src, dest) = (os.Path(args(0), os.pwd), os.Path(args(1), os.pwd))
    val agentExecutable = os.temp(os.read.bytes(os.resource / "agent.jar"))
    os.perms.set(agentExecutable, "rwx------")
    val agent = os.proc(agentExecutable).spawn(cwd = dest)
    case class HashStatInfo(localHash: Option[Int], path: os.SubPath)
    import castor.Context.Simple.global
    object SyncActor extends castor.SimpleActor[HashStatInfo]{
      val fileHashMap = collection.mutable.Map.empty[os.SubPath, Int]
      def run(msg: HashStatInfo): Unit = {
        println("SyncActor handling: " + msg)
        if (msg.localHash != fileHashMap.get(msg.path)) {
          msg.localHash match{
            case None => fileHashMap.remove(msg.path)
            case Some(hash) => fileHashMap(msg.path) = hash
          }
          if (msg.localHash.isDefined){
            Shared.send(agent.stdin.data, Rpc.WriteOver(os.read.bytes(src / msg.path), msg.path))
          }
        }
      }
    }
    object HashActor extends castor.SimpleActor[os.SubPath]{
      def run(path: os.SubPath): Unit = {
        println("HashActor handling: " + path)
        val localHash = Shared.hashPath(src / path)
        SyncActor.send(HashStatInfo(localHash, path))
      }
    }
    val watcher = os.watch.watch(
      Seq(src),
      onEvent = _.foreach(p => HashActor.send(p.subRelativeTo(src)))
    )
    Thread.sleep(Long.MaxValue)
  }
}
