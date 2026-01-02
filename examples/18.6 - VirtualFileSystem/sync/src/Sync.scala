package sync
object Sync:
  def main(src0: String, dest0: String): Unit =
    val (src, dest) = (os.Path(src0, os.pwd), os.Path(dest0, os.pwd))
    val agentExecutable = os.temp(os.read.bytes(os.resource / "agent.jar"))
    os.perms.set(agentExecutable, "rwx------")
    val agent = os.spawn(cmd = agentExecutable, cwd = dest)

    case class HashStatInfo(localHash: Option[Int], path: os.SubPath)

    import castor.Context.Simple.global
    object SyncActor extends castor.SimpleActor[HashStatInfo]:
      val fileHashMap = collection.mutable.Map.empty[os.SubPath, Int]
      def run(msg: HashStatInfo): Unit =
        if msg.localHash != fileHashMap.get(msg.path) then
          msg.localHash match
            case None => fileHashMap.remove(msg.path)
            case Some(hash) => fileHashMap(msg.path) = hash

          if msg.localHash.isDefined then
            Shared.send(agent.stdin.data, Rpc.WriteOver(os.read.bytes(src / msg.path), msg.path))

    object HashActor extends castor.SimpleActor[os.SubPath]:
      def run(path: os.SubPath): Unit =
        val localHash = Shared.hashPath(src / path)
        SyncActor.send(HashStatInfo(localHash, path))

    val watcher = os.watch.watch(
      Seq(src),
      onEvent = _.foreach(p => HashActor.send(p.subRelativeTo(src)))
    )
    Thread.sleep(Long.MaxValue)
