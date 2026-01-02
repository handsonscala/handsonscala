package sync

object Agent:
  @main def run(): Unit =
    val input = java.io.DataInputStream(System.in)
    val output = java.io.DataOutputStream(System.out)

    while true do try
      Shared.receive[Rpc](input)

    catch case e: java.io.EOFException => System.exit(0)