 > def sync(src: os.Path, dest: os.Path) =
-    { /* nothing yet */ }
+    for srcSubPath <- os.walk(src) do
+      val subPath = srcSubPath.subRelativeTo(src)
+      val destSubPath = dest / subPath
+      println((os.isDir(srcSubPath), os.isDir(destSubPath)))

 def sync(src: os.Path, dest: os.Path): Unit
