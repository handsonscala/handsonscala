package sync
import upickle.default.{ReadWriter, macroRW, readwriter}

sealed trait Rpc
object Rpc{
  implicit val subPathRw = readwriter[String].bimap[os.SubPath](_.toString, os.SubPath(_))

  case class StatPath(path: os.SubPath) extends Rpc
  implicit val statPathRw: ReadWriter[StatPath] = macroRW

  case class WriteOver(src: Array[Byte], path: os.SubPath) extends Rpc
  implicit val writeOverRw: ReadWriter[WriteOver] = macroRW

  case class StatInfo(p: os.SubPath, fileHash: Option[Int])
  implicit val statInfoRw: ReadWriter[StatInfo] = macroRW

  implicit val msgRw: ReadWriter[Rpc] = macroRW
}