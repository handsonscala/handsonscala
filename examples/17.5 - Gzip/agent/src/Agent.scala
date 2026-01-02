package sync

object Agent:
  @main def run(): Unit =
    val input = java.io.DataInputStream(java.util.zip.GZIPInputStream(System.in))
    val output = java.io.DataOutputStream(java.util.zip.GZIPOutputStream(System.out, true))

    while true do try
      Shared.receive[Rpc](input) match
        case Rpc.IsDir(path) => Shared.send(output, os.isDir(os.pwd / path))
        case Rpc.Exists(path) => Shared.send(output, os.exists(os.pwd / path))
        case Rpc.ReadBytes(path) => Shared.send(output, os.read.bytes(os.pwd / path))
        case Rpc.WriteOver(bytes, path) =>
          os.remove.all(os.pwd / path)
          Shared.send(output, os.write.over(os.pwd / path, bytes, createFolders = true))

    catch case e: java.io.EOFException => System.exit(0)
