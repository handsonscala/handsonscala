package sync

object Agent:
  @main def run(): Unit =
    val input = java.io.DataInputStream(System.in)
    val output = java.io.DataOutputStream(System.out)

    while true do try
      Shared.receive[Rpc](input) match
        case Rpc.StatPath(path) =>
          Shared.send(output, Rpc.StatInfo(path, Shared.hashPath(os.pwd / path)))

        case Rpc.WriteOver(bytes, path) =>
          os.remove.all(os.pwd / path)
          os.write.over(os.pwd / path, bytes, createFolders = true)

        case Rpc.Delete(path) => os.remove.all(os.pwd / path)

    catch case e: java.io.EOFException => System.exit(0)
