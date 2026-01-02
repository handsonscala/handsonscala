package sync

given subPathRw: upickle.ReadWriter[os.SubPath] =
  upickle.readwriter[String].bimap[os.SubPath](_.toString, os.SubPath(_))

enum Rpc derives upickle.ReadWriter:
  case StatPath(path: os.SubPath)
  case WriteOver(src: Array[Byte], path: os.SubPath)

object Rpc:
  case class StatInfo(p: os.SubPath, fileHash: Option[Int]) derives upickle.ReadWriter
