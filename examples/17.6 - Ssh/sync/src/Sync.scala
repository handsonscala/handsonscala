package sync
object Sync:
  def main(src0: String, dest0: String, remote: String): Unit =
    val src = os.Path(src0, os.pwd)
    val dest = os.RelPath(dest0)

    val agentExecutable = os.temp(os.read.bytes(os.resource / "agent.jar"))
    os.call(cmd = ("scp", agentExecutable, s"$remote:~/agent.jar"))
    os.call(cmd = ("ssh", remote, "chmod", "+x", "~/agent.jar"))
    os.call(cmd = ("ssh", remote, "mkdir", "-p", dest))
    val agent = os.spawn(cmd = ("ssh", remote, s"cd $dest; ~/agent.jar"))
    def callAgent[T: upickle.Reader](rpc: Rpc): () => T =
      Shared.send(agent.stdin.data, rpc)
      () => Shared.receive[T](agent.stdout.data)

    val subPaths = os.walk(src).map(_.subRelativeTo(src))
    def pipelineCalls[T: upickle.Reader](rpcFor: os.SubPath => Option[Rpc]) =
      val buffer = collection.mutable.Buffer.empty[(os.RelPath, () => T)]
      for p <- subPaths; rpc <- rpcFor(p) do buffer.addOne((p, callAgent[T](rpc)))
      buffer.map((k, v) => (k, v())).toMap

    val existsMap = pipelineCalls[Boolean](p => Some(Rpc.Exists(p)))
    val isDirMap = pipelineCalls[Boolean](p => Some(Rpc.IsDir(p)))
    val readMap = pipelineCalls[Array[Byte]]: p =>
      if existsMap(p) && !isDirMap(p) then Some(Rpc.ReadBytes(p))
      else None

    pipelineCalls[Unit]: p =>
      if os.isDir(src / p) then None
      else
        val localBytes = os.read.bytes(src / p)
        if readMap.get(p).exists(java.util.Arrays.equals(_, localBytes)) then None
        else Some(Rpc.WriteOver(localBytes, p))
