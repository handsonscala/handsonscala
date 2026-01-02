package sync

given subPathRw: upickle.ReadWriter[os.SubPath] =
  upickle.readwriter[String].bimap[os.SubPath](_.toString, os.SubPath(_))

enum Rpc derives upickle.ReadWriter:
  case WriteOver(src: Array[Byte], path: os.SubPath)
