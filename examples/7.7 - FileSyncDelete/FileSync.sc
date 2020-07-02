def sync(src: os.Path, dest: os.Path) = {
  val srcPaths = os.walk(src)
  for (srcPath <- srcPaths) {
    val subPath = srcPath.subRelativeTo(src)
    val destSubPath = dest / subPath
    (os.isDir(srcPath), os.isDir(destSubPath)) match {
      case (false, true) | (true, false) =>
        os.copy.over(srcPath, destSubPath, createFolders = true)
      case (false, false)
        if !os.exists(destSubPath)
        || !os.read.bytes(srcPath).sameElements(os.read.bytes(destSubPath)) =>

        os.copy.over(srcPath, destSubPath, createFolders = true)

      case _ => // do nothing
    }
  }
  val srcPathSet = srcPaths.map(_.subRelativeTo(src)).to(Set)
  for(destPath <- os.walk(dest)){
    val destSubPath = destPath.subRelativeTo(dest)
    if (!srcPathSet.contains(destSubPath)) os.remove.all(destPath)
  }
}
