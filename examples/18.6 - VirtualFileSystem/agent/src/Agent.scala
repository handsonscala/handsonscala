package sync

object Agent:
  @main def run(): Unit =
    val input = java.io.DataInputStream(System.in)
    val output = java.io.DataOutputStream(System.out)

    while true do try
      Shared.receive[Rpc](input) match
        case Rpc.WriteOver(bytes, path) =>
          os.remove.all(os.pwd / path)
          os.write.over(os.pwd / path, bytes, createFolders = true)

    catch case e: java.io.EOFException => System.exit(0)
