package sync
object Sync:
  def main(src0: String, dest0: String): Unit =
    val src = os.Path(src0, os.pwd)
    val dest = os.Path(dest0, os.pwd)
    for srcSubPath <- os.walk(src) do
      val subPath = srcSubPath.subRelativeTo(src)
      val destSubPath = dest / subPath
      (os.isDir(srcSubPath), os.isDir(destSubPath)) match
        case (false, true) | (true, false) =>
          os.copy.over(srcSubPath, destSubPath, createFolders = true)

        case (false, false)
          if !os.exists(destSubPath)
          || !os.read.bytes(srcSubPath).sameElements(os.read.bytes(destSubPath)) =>

          os.copy.over(srcSubPath, destSubPath, createFolders = true)

        case _ => // do nothing
