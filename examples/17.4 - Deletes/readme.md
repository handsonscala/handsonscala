# Example 17.4 - Deletes
Pipelined two-process file synchronizer that supports deletions

```bash
./mill -i sync.test
```

## Upstream Example: [17.3 - Pipelined](https://github.com/handsonscala/handsonscala/tree/v2/examples/17.3%20-%20Pipelined):
Diff:
```diff
diff --git a/17.3 - Pipelined/agent/src/Agent.scala b/17.4 - Deletes/agent/src/Agent.scala
index b0936dd..284cb15 100644
--- a/17.3 - Pipelined/agent/src/Agent.scala	
+++ b/17.4 - Deletes/agent/src/Agent.scala	
@@ -10,6 +10,8 @@ object Agent:
         case Rpc.IsDir(path) => Shared.send(output, os.isDir(os.pwd / path))
         case Rpc.Exists(path) => Shared.send(output, os.exists(os.pwd / path))
         case Rpc.ReadBytes(path) => Shared.send(output, os.read.bytes(os.pwd / path))
+        case Rpc.Delete(path) => Shared.send(output, {os.remove(os.pwd / path); ()})
+        case Rpc.RemoteScan() => Shared.send(output, os.walk(os.pwd).map(_.subRelativeTo(os.pwd)))
         case Rpc.WriteOver(bytes, path) =>
           os.remove.all(os.pwd / path)
           Shared.send(output, os.write.over(os.pwd / path, bytes, createFolders = true))
diff --git a/17.3 - Pipelined/shared/src/Rpc.scala b/17.4 - Deletes/shared/src/Rpc.scala
index 6a3328b..a7d7c75 100644
--- a/17.3 - Pipelined/shared/src/Rpc.scala	
+++ b/17.4 - Deletes/shared/src/Rpc.scala	
@@ -7,4 +7,6 @@ enum Rpc derives upickle.ReadWriter:
   case IsDir(path: os.SubPath)
   case Exists(path: os.SubPath)
   case ReadBytes(path: os.SubPath)
+  case RemoteScan()
   case WriteOver(src: Array[Byte], path: os.SubPath)
+  case Delete(path: os.SubPath)
diff --git a/17.3 - Pipelined/sync/src/Sync.scala b/17.4 - Deletes/sync/src/Sync.scala
index c9514ed..492d99b 100644
--- a/17.3 - Pipelined/sync/src/Sync.scala	
+++ b/17.4 - Deletes/sync/src/Sync.scala	
@@ -1,4 +1,5 @@
 package sync
+
 object Sync:
   def main(src0: String, dest0: String): Unit =
     val src = os.Path(src0, os.pwd)
@@ -12,21 +13,27 @@ object Sync:
       () => Shared.receive[T](agent.stdout.data)
 
     val subPaths = os.walk(src).map(_.subRelativeTo(src))
+    val subPathSet = subPaths.toSet
 
-    def pipelineCalls[T: upickle.Reader](rpcFor: os.SubPath => Option[Rpc]) =
-      val buffer = collection.mutable.Buffer.empty[(os.RelPath, () => T)]
-      for p <- subPaths; rpc <- rpcFor(p) do buffer.addOne((p, callAgent[T](rpc)))
+    def pipelineCalls[T: upickle.Reader](paths: Seq[os.SubPath])
+                                        (rpcFor: os.SubPath => Option[Rpc]) =
+      val buffer = collection.mutable.Buffer.empty[(os.SubPath, () => T)]
+      for p <- paths; rpc <- rpcFor(p) do buffer.append((p, callAgent[T](rpc)))
       buffer.map((k, v) => (k, v())).toMap
 
-    val existsMap = pipelineCalls[Boolean](p => Some(Rpc.Exists(p)))
-    val isDirMap = pipelineCalls[Boolean](p => Some(Rpc.IsDir(p)))
-
-    val readMap = pipelineCalls[Array[Byte]]: p =>
+    val existsMap = pipelineCalls[Boolean](subPaths)(p => Some(Rpc.Exists(p)))
+    val isDirMap = pipelineCalls[Boolean](subPaths)(p => Some(Rpc.IsDir(p)))
+    val readMap = pipelineCalls[Array[Byte]](subPaths): p =>
       if existsMap(p) && !isDirMap(p) then Some(Rpc.ReadBytes(p))
       else None
 
-    pipelineCalls[Unit]: p =>
+    val remoteScanned = callAgent[Seq[os.SubPath]](Rpc.RemoteScan()).apply()
+
+    val allPaths = (subPaths ++ remoteScanned).distinct
+
+    pipelineCalls[Unit](allPaths): p =>
       if os.isDir(src / p) then None
+      else if !subPathSet.contains(p) then Some(Rpc.Delete(p))
       else
         val localBytes = os.read.bytes(src / p)
         if readMap.get(p).exists(java.util.Arrays.equals(_, localBytes)) then None
diff --git a/17.3 - Pipelined/sync/test/src/SyncTests.scala b/17.4 - Deletes/sync/test/src/SyncTests.scala
index 3ebacd0..b84db0c 100644
--- a/17.3 - Pipelined/sync/test/src/SyncTests.scala	
+++ b/17.4 - Deletes/sync/test/src/SyncTests.scala	
@@ -28,3 +28,13 @@ object SyncTests extends TestSuite:
       println("SECOND VALIDATION")
       assert(os.read(dest / "folder1/hello.txt") == "hello")
       assert(os.read(dest /  "folder1/nested/world.txt") == "WORLD")
+
+      println("DELETE SRC FILE")
+      os.remove(src / "folder1/hello.txt")
+
+      println("DELETE SYNC")
+      Sync.main(src.toString, dest.toString)
+
+      println("DELETE VALIDATION")
+      assert(!os.exists(dest / "folder1/hello.txt"))
+      assert(os.read(dest /  "folder1/nested/world.txt") == "WORLD")
```
