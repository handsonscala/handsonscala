package sync
object Agent {
  def main(args: Array[String]): Unit = {
    val input = new java.io.DataInputStream(System.in)
    val output = new java.io.DataOutputStream(System.out)
    while (true) try {
      val rpc = Shared.receive[Rpc](input)
    } catch {case e: java.io.EOFException => System.exit(0)}
  }
}