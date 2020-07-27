package sync
object Agent {
  def main(args: Array[String]): Unit = {
    val input = new java.io.DataInputStream(System.in)
    val output = new java.io.DataOutputStream(System.out)
    while (true) try {
      val rpc = Shared.receive[Rpc](input)
      System.err.println("Agent handling: " + rpc)
      rpc match {
        case Rpc.StatPath(path) =>
          Shared.send(output, Rpc.StatInfo(path, Shared.hashPath(os.pwd / path)))

        case Rpc.WriteOver(bytes, path) =>
          os.remove.all(os.pwd / path)
          os.write.over(os.pwd / path, bytes, createFolders = true)
      }
    } catch { case e: java.io.EOFException => System.exit(0) }
  }
}