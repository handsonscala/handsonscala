package sync
object Sync:
  def main(src0: String, dest0: String): Unit =
    val src = os.Path(src0, os.pwd)
    val dest = os.Path(dest0, os.pwd)

    val agentExecutable = os.temp(os.read.bytes(os.resource / "agent.jar"))
    os.perms.set(agentExecutable, "rwx------")
    val agent = os.spawn(cmd = agentExecutable, cwd = dest)
    def callAgent[T: upickle.Reader](rpc: Rpc): T =
      Shared.send(agent.stdin.data, rpc)
      Shared.receive[T](agent.stdout.data)

    for srcSubPath <- os.walk(src) do
      val subPath = srcSubPath.subRelativeTo(src)
      val destSubPath = dest / subPath
      (os.isDir(srcSubPath), callAgent[Boolean](Rpc.IsDir(subPath))) match
        case (false, true) =>
          callAgent[Unit](Rpc.WriteOver(os.read.bytes(srcSubPath), subPath))

        case (true, false) =>
          for p <- os.walk(srcSubPath) if os.isFile(p) do
            callAgent[Unit](Rpc.WriteOver(os.read.bytes(p), p.subRelativeTo(src)))

        case (false, false)
          if !callAgent[Boolean](Rpc.Exists(subPath))
          || !os.read.bytes(srcSubPath).sameElements(
            callAgent[Array[Byte]](Rpc.ReadBytes(subPath))
          ) =>

          callAgent[Unit](Rpc.WriteOver(os.read.bytes(srcSubPath), subPath))

        case _ => // do nothing