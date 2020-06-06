package sync
object Sync {
  def main(args: Array[String]): Unit = {
    val src = os.Path(args(0), os.pwd)
    val dest = os.Path(args(1), os.pwd)
    for (srcSubPath <- os.walk(src)) {
      val subPath = srcSubPath.subRelativeTo(src)
      val destSubPath = dest / subPath
      (os.isDir(srcSubPath), os.isDir(destSubPath)) match {
        case (false, true) | (true, false) => os.copy.over(srcSubPath, destSubPath)
        case (false, false)
          if !os.exists(destSubPath)
            || !os.read.bytes(srcSubPath).sameElements(os.read.bytes(destSubPath)) =>

          os.copy.over(srcSubPath, destSubPath, createFolders = true)

        case _ => // do nothing
      }
    }
  }
}
