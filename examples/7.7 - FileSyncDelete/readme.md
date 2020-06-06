# Example 7.7 - FileSyncDelete
Method to synchronize files between two folders, with support for deletion

```bash
amm TestFileSync.sc
```

## Upstream Example: [7.2 - FileSync](https://github.com/handsonscala/handsonscala/tree/v1/examples/7.2%20-%20FileSync):
Diff:
```diff
diff --git a/7.2 - FileSync/FileSync.sc b/7.7 - FileSyncDelete/FileSync.sc
index c566695..31fa48d 100644
--- a/7.2 - FileSync/FileSync.sc	
+++ b/7.7 - FileSyncDelete/FileSync.sc	
@@ -1,17 +1,23 @@
 def sync(src: os.Path, dest: os.Path) = {
-  for (srcSubPath <- os.walk(src)) {
-    val subPath = srcSubPath.subRelativeTo(src)
+  val srcPaths = os.walk(src)
+  for (srcPath <- srcPaths) {
+    val subPath = srcPath.subRelativeTo(src)
     val destSubPath = dest / subPath
-    (os.isDir(srcSubPath), os.isDir(destSubPath)) match {
+    (os.isDir(srcPath), os.isDir(destSubPath)) match {
       case (false, true) | (true, false) =>
-        os.copy.over(srcSubPath, destSubPath, createFolders = true)
+        os.copy.over(srcPath, destSubPath, createFolders = true)
       case (false, false)
         if !os.exists(destSubPath)
-        || !os.read.bytes(srcSubPath).sameElements(os.read.bytes(destSubPath)) =>
+        || !os.read.bytes(srcPath).sameElements(os.read.bytes(destSubPath)) =>
 
-        os.copy.over(srcSubPath, destSubPath, createFolders = true)
+        os.copy.over(srcPath, destSubPath, createFolders = true)
 
       case _ => // do nothing
     }
   }
+  val srcPathSet = srcPaths.map(_.subRelativeTo(src)).to(Set)
+  for(destPath <- os.walk(dest)){
+    val destSubPath = destPath.subRelativeTo(dest)
+    if (!srcPathSet.contains(destSubPath)) os.remove.all(destPath)
+  }
 }
diff --git a/7.2 - FileSync/TestFileSync.sc b/7.7 - FileSyncDelete/TestFileSync.sc
index 21c8e33..cfccdb1 100644
--- a/7.2 - FileSync/TestFileSync.sc	
+++ b/7.7 - FileSyncDelete/TestFileSync.sc	
@@ -25,3 +25,13 @@ sync(src, dest)
 println("SECOND VALIDATION")
 assert(os.read(dest / "folder1" / "hello.txt") == "hello")
 assert(os.read(dest /  "folder1" / "nested" / "world.txt") == "WORLD")
+
+println("DELETE SRC FILE")
+os.remove(src / "folder1" / "hello.txt")
+
+println("DELETE SYNC")
+sync(src, dest)
+
+println("DELETE VALIDATION")
+assert(!os.exists(dest / "folder1" / "hello.txt"))
+assert(os.read(dest /  "folder1" / "nested" / "world.txt") == "WORLD")
```
