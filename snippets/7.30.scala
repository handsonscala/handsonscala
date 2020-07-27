 @ def sync(src: os.Path, dest: os.Path) = {
+    for (srcSubPath <- os.walk(src)) {
+      val subPath = srcSubPath.subRelativeTo(src)
+      val destSubPath = dest / subPath
+      println((os.isDir(srcSubPath), os.isDir(destSubPath)))
+    }
   }
