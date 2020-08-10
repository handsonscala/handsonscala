package sync
import upickle.default.{ReadWriter, macroRW, readwriter}

sealed trait Rpc
object Rpc{
  implicit val subPathRw = readwriter[String].bimap[os.SubPath](_.toString, os.SubPath(_))

  case class WriteOver(src: Array[Byte], path: os.SubPath) extends Rpc
  implicit val writeOverRw: ReadWriter[WriteOver] = macroRW

  implicit val msgRw: ReadWriter[Rpc] = macroRW
}
