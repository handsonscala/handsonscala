
class Msg(val id: Int, val parent: Option[Int], val txt: String)

def printMessages(messages: Array[Msg]): Unit = {
  def printFrag(parent: Option[Int], indent: String): Unit = {
    for (msg <- messages if msg.parent == parent) {
      println(s"$indent#${msg.id} ${msg.txt}")
      printFrag(Some(msg.id), indent + "    ")
    }
  }
  printFrag(None, "")
}
