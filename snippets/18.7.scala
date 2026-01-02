package sync
object Sync:
  def main(src0: String, dest0: String): Unit =
    val (src, dest) = (os.Path(src0, os.pwd), os.Path(dest0, os.pwd))
    val agentExecutable = os.temp(os.read.bytes(os.resource / "agent.jar"))
    os.perms.set(agentExecutable, "rwx------")
    val agent = os.spawn(cmd = agentExecutable, cwd = dest)