# Example 17.4 - Deletes
Pipelined two-process file synchronizer that supports deletions

```bash
./mill -i sync.test
```

## Upstream Example: [17.3 - Pipelined](https://github.com/handsonscala/handsonscala/tree/v1/examples/17.3%20-%20Pipelined):
Diff:
```diff
diff --git a/17.3 - Pipelined/agent/src/Agent.scala b/17.4 - Deletes/agent/src/Agent.scala
index a7d17cc..7d81bb9 100644
--- a/17.3 - Pipelined/agent/src/Agent.scala	
+++ b/17.4 - Deletes/agent/src/Agent.scala	
@@ -1,4 +1,5 @@
 package sync
+import Rpc.subPathRw
 object Agent {
   def main(args: Array[String]): Unit = {
     val input = new java.io.DataInputStream(System.in)
@@ -9,6 +10,8 @@ object Agent {
         case Rpc.IsDir(path) => Shared.send(output, os.isDir(os.pwd / path))
         case Rpc.Exists(path) => Shared.send(output, os.exists(os.pwd / path))
         case Rpc.ReadBytes(path) => Shared.send(output, os.read.bytes(os.pwd / path))
+        case Rpc.Delete(path) => Shared.send(output, os.remove(os.pwd / path))
+        case Rpc.RemoteScan() => Shared.send(output, os.walk(os.pwd).map(_.subRelativeTo(os.pwd)))
         case Rpc.WriteOver(bytes, path) =>
           os.remove.all(os.pwd / path)
           Shared.send(output, os.write.over(os.pwd / path, bytes, createFolders = true))
diff --git a/17.3 - Pipelined/build.sc b/17.4 - Deletes/build.sc
index f1e60ef..983c035 100644
--- a/17.3 - Pipelined/build.sc	
+++ b/17.4 - Deletes/build.sc	
@@ -19,7 +19,7 @@ object sync extends SyncModule{
   object test extends Tests{ // Test Suite
     def testFrameworks = Seq("utest.runner.Framework")
     def ivyDeps = Agg(ivy"com.lihaoyi::utest:0.7.4")
-  }
+  }                                                  // Test Suite
 }
 object agent extends SyncModule{
   def moduleDeps = Seq(shared)
diff --git a/17.3 - Pipelined/shared/src/Rpc.scala b/17.4 - Deletes/shared/src/Rpc.scala
index b87163d..ed06878 100644
--- a/17.3 - Pipelined/shared/src/Rpc.scala	
+++ b/17.4 - Deletes/shared/src/Rpc.scala	
@@ -13,8 +13,14 @@ object Rpc{
   case class ReadBytes(path: os.SubPath) extends Rpc
   implicit val readBytesRw: ReadWriter[ReadBytes] = macroRW
 
+  case class RemoteScan() extends Rpc
+  implicit val remoteScanRw: ReadWriter[RemoteScan] = macroRW
+
   case class WriteOver(src: Array[Byte], path: os.SubPath) extends Rpc
   implicit val writeOverRw: ReadWriter[WriteOver] = macroRW
 
+  case class Delete(path: os.SubPath) extends Rpc
+  implicit val deleteRw: ReadWriter[Delete] = macroRW
+
   implicit val RpcRw: ReadWriter[Rpc] = macroRW
 }
diff --git a/17.3 - Pipelined/sync/src/Sync.scala b/17.4 - Deletes/sync/src/Sync.scala
index 367c163..f257e82 100644
--- a/17.3 - Pipelined/sync/src/Sync.scala	
+++ b/17.4 - Deletes/sync/src/Sync.scala	
@@ -1,4 +1,5 @@
 package sync
+import Rpc.subPathRw
 object Sync {
   def main(args: Array[String]): Unit = {
     val src = os.Path(args(0), os.pwd)
@@ -11,19 +12,29 @@ object Sync {
       () => Shared.receive[T](agent.stdout.data)
     }
     val subPaths = os.walk(src).map(_.subRelativeTo(src))
-    def pipelineCalls[T: upickle.default.Reader](rpcFor: os.SubPath => Option[Rpc]) = {
-      val buffer = collection.mutable.Buffer.empty[(os.RelPath, () => T)]
-      for (p <- subPaths; rpc <- rpcFor (p)) buffer.append((p, callAgent[T](rpc)))
+    val subPathSet = subPaths.toSet
+    def pipelineCalls[T: upickle.default.Reader](paths: Seq[os.SubPath])
+                                                (rpcFor: os.SubPath => Option[Rpc]) = {
+      val buffer = collection.mutable.Buffer.empty[(os.SubPath, () => T)]
+      for (p <- paths; rpc <- rpcFor (p)) buffer.append((p, callAgent[T](rpc)))
       buffer.map{case (k, v) => (k, v())}.toMap
     }
-    val existsMap = pipelineCalls[Boolean](p => Some(Rpc.Exists(p)))
-    val isDirMap = pipelineCalls[Boolean](p => Some(Rpc.IsDir(p)))
-    val readMap = pipelineCalls[Array[Byte]]{p =>
+
+    val existsMap = pipelineCalls[Boolean](subPaths)(p => Some(Rpc.Exists(p)))
+    val isDirMap = pipelineCalls[Boolean](subPaths)(p => Some(Rpc.IsDir(p)))
+
+    val readMap = pipelineCalls[Array[Byte]](subPaths){p =>
       if (existsMap(p) && !isDirMap(p)) Some(Rpc.ReadBytes(p))
       else None
     }
-    pipelineCalls[Unit]{ p =>
+
+    val remoteScanned = callAgent[Seq[os.SubPath]](Rpc.RemoteScan()).apply()
+
+    val allPaths = (subPaths ++ remoteScanned).distinct
+
+    pipelineCalls[Unit](allPaths){ p =>
       if (os.isDir(src / p)) None
+      else if (!subPathSet.contains(p)) Some(Rpc.Delete(p))
       else {
         val localBytes = os.read.bytes(src / p)
         if (readMap.get(p).exists(java.util.Arrays.equals(_, localBytes))) None
diff --git a/17.3 - Pipelined/sync/test/src/SyncTests.scala b/17.4 - Deletes/sync/test/src/SyncTests.scala
index 1582ddf..9e2ef3c 100644
--- a/17.3 - Pipelined/sync/test/src/SyncTests.scala	
+++ b/17.4 - Deletes/sync/test/src/SyncTests.scala	
@@ -28,6 +28,16 @@ object SyncTests extends TestSuite{
       println("SECOND VALIDATION")
       assert(os.read(dest / "folder1" / "hello.txt") == "hello")
       assert(os.read(dest /  "folder1" / "nested" / "world.txt") == "WORLD")
+
+      println("DELETE SRC FILE")
+      os.remove(src / "folder1" / "hello.txt")
+
+      println("DELETE SYNC")
+      Sync.main(Array(src.toString, dest.toString))
+
+      println("DELETE VALIDATION")
+      assert(!os.exists(dest / "folder1" / "hello.txt"))
+      assert(os.read(dest /  "folder1" / "nested" / "world.txt") == "WORLD")
     }
   }
 }
```
