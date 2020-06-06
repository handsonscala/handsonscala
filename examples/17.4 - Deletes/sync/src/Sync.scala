package sync
import Rpc.subPathRw
object Sync {
  def main(args: Array[String]): Unit = {
    val src = os.Path(args(0), os.pwd)
    val dest = os.Path(args(1), os.pwd)
    val agentExecutable = os.temp(os.read.bytes(os.resource / "agent.jar"))
    os.perms.set(agentExecutable, "rwx------")
    val agent = os.proc(agentExecutable).spawn(cwd = dest)
    def callAgent[T: upickle.default.Reader](rpc: Rpc): () => T = {
      Shared.send(agent.stdin.data, rpc)
      () => Shared.receive[T](agent.stdout.data)
    }
    val subPaths = os.walk(src).map(_.subRelativeTo(src))
    val subPathSet = subPaths.toSet
    def pipelineCalls[T: upickle.default.Reader](paths: Seq[os.SubPath])
                                                (rpcFor: os.SubPath => Option[Rpc]) = {
      val buffer = collection.mutable.Buffer.empty[(os.SubPath, () => T)]
      for (p <- paths; rpc <- rpcFor (p)) buffer.append((p, callAgent[T](rpc)))
      buffer.map{case (k, v) => (k, v())}.toMap
    }

    val existsMap = pipelineCalls[Boolean](subPaths)(p => Some(Rpc.Exists(p)))
    val isDirMap = pipelineCalls[Boolean](subPaths)(p => Some(Rpc.IsDir(p)))

    val readMap = pipelineCalls[Array[Byte]](subPaths){p =>
      if (existsMap(p) && !isDirMap(p)) Some(Rpc.ReadBytes(p))
      else None
    }

    val remoteScanned = callAgent[Seq[os.SubPath]](Rpc.RemoteScan()).apply()

    val allPaths = (subPaths ++ remoteScanned).distinct

    pipelineCalls[Unit](allPaths){ p =>
      if (os.isDir(src / p)) None
      else if (!subPathSet.contains(p)) Some(Rpc.Delete(p))
      else {
        val localBytes = os.read.bytes(src / p)
        if (readMap.get(p).exists(java.util.Arrays.equals(_, localBytes))) None
        else Some(Rpc.WriteOver(localBytes, p))
      }
    }
  }
}
