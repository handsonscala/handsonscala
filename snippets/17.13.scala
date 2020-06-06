       val subPath = srcSubPath.subRelativeTo(src)
       val destSubPath = dest / subPath
-      (os.isDir(srcSubPath), os.isDir(destSubPath)) match {
+      (os.isDir(srcSubPath), callAgent[Boolean](Rpc.IsDir(subPath))) match {
-        case (false, true) | (true, false) => os.copy.over(srcSubPath, destSubPath)
+        case (false, true) =>
+          callAgent[Unit](Rpc.WriteOver(os.read.bytes(srcSubPath), subPath))
+        case (true, false) =>
+          for (p <- os.walk(srcSubPath) if os.isFile(p)) {
+            callAgent[Unit](Rpc.WriteOver(os.read.bytes(p), p.subRelativeTo(src)))
+          }
         case (false, false)
-          if !os.exists(destSubPath)
-          || !os.read.bytes(srcSubPath).sameElements(os.read.bytes(destSubPath)) =>
+          if !callAgent[Boolean](Rpc.Exists(subPath))
+          || !os.read.bytes(srcSubPath).sameElements(
+               callAgent[Array[Byte]](Rpc.ReadBytes(subPath))
+             ) =>

-          os.copy.over(srcSubPath, destSubPath, createFolders = true)
+          callAgent[Unit](Rpc.WriteOver(os.read.bytes(srcSubPath), subPath))

         case _ => // do nothing
       }