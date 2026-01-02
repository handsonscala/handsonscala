package sync

given subPathRw: upickle.ReadWriter[os.SubPath] =
  upickle.readwriter[String].bimap[os.SubPath](_.toString, os.SubPath(_))

enum Rpc derives upickle.ReadWriter:
  case IsDir(path: os.SubPath)
  case Exists(path: os.SubPath)
  case ReadBytes(path: os.SubPath)
  case RemoteScan()
  case WriteOver(src: Array[Byte], path: os.SubPath)
  case Delete(path: os.SubPath)
