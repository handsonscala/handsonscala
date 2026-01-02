 > def sync(src: os.Path, dest: os.Path) =
     for srcSubPath <- os.walk(src) do
       val subPath = srcSubPath.subRelativeTo(src)
       val destSubPath = dest / subPath
-      println((os.isDir(srcSubPath), os.isDir(destSubPath)))
+      (os.isDir(srcSubPath), os.isDir(destSubPath)) match
+        case (false, true) | (true, false) =>
+          os.copy.over(srcSubPath, destSubPath, createFolders = true)
+
+        case _ => // do nothing

 def sync(src: os.Path, dest: os.Path): Unit
