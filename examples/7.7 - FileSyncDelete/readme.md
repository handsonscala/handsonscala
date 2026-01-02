# Example 7.7 - FileSyncDelete
Method to synchronize files between two folders, with support for deletion

```bash
./mill -i TestFileSync.scala
```

## Upstream Example: [7.2 - FileSync](https://github.com/handsonscala/handsonscala/tree/v2/examples/7.2%20-%20FileSync):
Diff:
```diff
diff --git a/7.2 - FileSync/FileSync.scala b/7.7 - FileSyncDelete/FileSync.scala
index b7b2e7b..2c11798 100644
--- a/7.2 - FileSync/FileSync.scala	
+++ b/7.7 - FileSyncDelete/FileSync.scala	
@@ -1,15 +1,19 @@
 def sync(src: os.Path, dest: os.Path) =
-  for srcSubPath <- os.walk(src) do
-    val subPath = srcSubPath.subRelativeTo(src)
+  val srcPaths = os.walk(src)
+  for srcPath <- srcPaths do
+    val subPath = srcPath.subRelativeTo(src)
     val destSubPath = dest / subPath
-    (os.isDir(srcSubPath), os.isDir(destSubPath)) match
+    (os.isDir(srcPath), os.isDir(destSubPath)) match
       case (false, true) | (true, false) =>
-        os.copy.over(srcSubPath, destSubPath, createFolders = true)
-
+        os.copy.over(srcPath, destSubPath, createFolders = true)
       case (false, false)
         if !os.exists(destSubPath)
-        || !os.read.bytes(srcSubPath).sameElements(os.read.bytes(destSubPath)) =>
+        || !os.read.bytes(srcPath).sameElements(os.read.bytes(destSubPath)) =>
 
-        os.copy.over(srcSubPath, destSubPath, createFolders = true)
+        os.copy.over(srcPath, destSubPath, createFolders = true)
 
       case _ => // do nothing
+  val srcPathSet = srcPaths.map(_.subRelativeTo(src)).to(Set)
+  for destPath <- os.walk(dest) do
+    val destSubPath = destPath.subRelativeTo(dest)
+    if !srcPathSet.contains(destSubPath) then os.remove.all(destPath)
diff --git a/7.2 - FileSync/TestFileSync.scala b/7.7 - FileSyncDelete/TestFileSync.scala
index b6b1c89..af9b844 100644
--- a/7.2 - FileSync/TestFileSync.scala	
+++ b/7.7 - FileSyncDelete/TestFileSync.scala	
@@ -26,3 +26,13 @@ def main() =
   println("SECOND VALIDATION")
   assert(os.read(dest / "folder1/hello.txt") == "hello")
   assert(os.read(dest /  "folder1/nested/world.txt") == "WORLD")
+
+  println("DELETE SRC FILE")
+  os.remove(src / "folder1/hello.txt")
+
+  println("DELETE SYNC")
+  sync(src, dest)
+
+  println("DELETE VALIDATION")
+  assert(!os.exists(dest / "folder1/hello.txt"))
+  assert(os.read(dest /  "folder1/nested/world.txt") == "WORLD")
```
