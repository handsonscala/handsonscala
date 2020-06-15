package sync
import upickle.default.{readwriter, ReadWriter, macroRW}
sealed trait Rpc
object Rpc{
  implicit val subPathRw = readwriter[String].bimap[os.SubPath](_.toString, os.SubPath(_))

  case class IsDir(path: os.SubPath) extends Rpc
  implicit val isDirRw: ReadWriter[IsDir] = macroRW

  case class Exists(path: os.SubPath) extends Rpc
  implicit val existsRw: ReadWriter[Exists] = macroRW

  case class ReadBytes(path: os.SubPath) extends Rpc
  implicit val readBytesRw: ReadWriter[ReadBytes] = macroRW

  case class WriteOver(src: Array[Byte], path: os.SubPath) extends Rpc
  implicit val writeOverRw: ReadWriter[WriteOver] = macroRW

  implicit val RpcRw: ReadWriter[Rpc] = macroRW
}
