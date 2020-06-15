# Example 18.3 - InitialFiles
Real-time file synchronizer that supports syncing an initial set of files

```bash
./mill -i sync.test
```

## Upstream Example: [18.2 - Pipelined](https://github.com/handsonscala/handsonscala/tree/v1/examples/18.2%20-%20Pipelined):
Diff:
```diff
diff --git a/18.2 - Pipelined/sync/src/Sync.scala b/18.3 - InitialFiles/sync/src/Sync.scala
index c842c04..cefcabc 100644
--- a/18.2 - Pipelined/sync/src/Sync.scala	
+++ b/18.3 - InitialFiles/sync/src/Sync.scala	
@@ -38,6 +38,14 @@ object Sync {
       Seq(src),
       onEvent = _.foreach(p => SyncActor.send(ChangedPath(p.subRelativeTo(src))))
     )
+    // Scan the initial folders after we begin watching for changes, to avoid
+    // leaving a window in between scanning and watching where file changes may
+    // go un-noticed
+    for(p <- os.walk(src)) {
+      println("INITIAL SCAN " + p)
+      SyncActor.send(ChangedPath(p.subRelativeTo(src)))
+    }
+
     Thread.sleep(Long.MaxValue)
   }
 }
diff --git a/18.2 - Pipelined/sync/test/src/SyncTests.scala b/18.3 - InitialFiles/sync/test/src/SyncTests.scala
index 254a888..ffcbd09 100644
--- a/18.2 - Pipelined/sync/test/src/SyncTests.scala	
+++ b/18.3 - InitialFiles/sync/test/src/SyncTests.scala	
@@ -6,6 +6,11 @@ object SyncTests extends TestSuite{
 
       println("INITIALIZING SRC AND DEST")
       val src = os.temp.dir(os.pwd / "out")
+      os.write(src / "initial1.txt", "initial file 1")
+      os.makeDir(src / "initial-folder")
+      os.write(src / "initial-folder" / "initial2.txt", "initial file 2")
+      os.write(src / "initial-folder" / "initial3.txt", "initial file 3")
+
       val dest = os.temp.dir(os.pwd / "out")
 
       val syncThread = new Thread(() => Sync.main(Array(src.toString, dest.toString)))
@@ -19,6 +24,9 @@ object SyncTests extends TestSuite{
 
       Thread.sleep(1000)
       println("FIRST VALIDATION")
+      assert(os.read(dest / "initial1.txt") == "initial file 1")
+      assert(os.read(dest / "initial-folder" / "initial2.txt") == "initial file 2")
+      assert(os.read(dest / "initial-folder" / "initial3.txt") == "initial file 3")
       assert(os.read(dest / "folder1" / "hello.txt") == "HELLO")
       assert(os.read(dest /  "folder1" / "nested" / "world.txt") == "world")
 
```
