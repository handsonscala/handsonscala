# Example 17.3 - Pipelined
Pipelined version of our two-process file synchronizer, minimizing the
chattiness of the protocol

```bash
./mill -i sync.test
```

## Upstream Example: [17.2 - FileSyncer](https://github.com/handsonscala/handsonscala/tree/v2/examples/17.2%20-%20FileSyncer):
Diff:
```diff
diff --git a/17.2 - FileSyncer/sync/src/Sync.scala b/17.3 - Pipelined/sync/src/Sync.scala
index d4f8c01..c9514ed 100644
--- a/17.2 - FileSyncer/sync/src/Sync.scala	
+++ b/17.3 - Pipelined/sync/src/Sync.scala	
@@ -7,27 +7,27 @@ object Sync:
     val agentExecutable = os.temp(os.read.bytes(os.resource / "agent.jar"))
     os.perms.set(agentExecutable, "rwx------")
     val agent = os.spawn(cmd = agentExecutable, cwd = dest)
-    def callAgent[T: upickle.Reader](rpc: Rpc): T =
+    def callAgent[T: upickle.Reader](rpc: Rpc): () => T =
       Shared.send(agent.stdin.data, rpc)
-      Shared.receive[T](agent.stdout.data)
+      () => Shared.receive[T](agent.stdout.data)
 
-    for srcSubPath <- os.walk(src) do
-      val subPath = srcSubPath.subRelativeTo(src)
-      val destSubPath = dest / subPath
-      (os.isDir(srcSubPath), callAgent[Boolean](Rpc.IsDir(subPath))) match
-        case (false, true) =>
-          callAgent[Unit](Rpc.WriteOver(os.read.bytes(srcSubPath), subPath))
+    val subPaths = os.walk(src).map(_.subRelativeTo(src))
 
-        case (true, false) =>
-          for p <- os.walk(srcSubPath) if os.isFile(p) do
-            callAgent[Unit](Rpc.WriteOver(os.read.bytes(p), p.subRelativeTo(src)))
+    def pipelineCalls[T: upickle.Reader](rpcFor: os.SubPath => Option[Rpc]) =
+      val buffer = collection.mutable.Buffer.empty[(os.RelPath, () => T)]
+      for p <- subPaths; rpc <- rpcFor(p) do buffer.addOne((p, callAgent[T](rpc)))
+      buffer.map((k, v) => (k, v())).toMap
 
-        case (false, false)
-          if !callAgent[Boolean](Rpc.Exists(subPath))
-          || !os.read.bytes(srcSubPath).sameElements(
-            callAgent[Array[Byte]](Rpc.ReadBytes(subPath))
-          ) =>
+    val existsMap = pipelineCalls[Boolean](p => Some(Rpc.Exists(p)))
+    val isDirMap = pipelineCalls[Boolean](p => Some(Rpc.IsDir(p)))
 
-          callAgent[Unit](Rpc.WriteOver(os.read.bytes(srcSubPath), subPath))
+    val readMap = pipelineCalls[Array[Byte]]: p =>
+      if existsMap(p) && !isDirMap(p) then Some(Rpc.ReadBytes(p))
+      else None
 
-        case _ => // do nothing
+    pipelineCalls[Unit]: p =>
+      if os.isDir(src / p) then None
+      else
+        val localBytes = os.read.bytes(src / p)
+        if readMap.get(p).exists(java.util.Arrays.equals(_, localBytes)) then None
+        else Some(Rpc.WriteOver(localBytes, p))
diff --git a/17.2 - FileSyncer/sync/test/src/SyncTests.scala b/17.3 - Pipelined/sync/test/src/SyncTests.scala
index 04d0a4e..3ebacd0 100644
--- a/17.2 - FileSyncer/sync/test/src/SyncTests.scala	
+++ b/17.3 - Pipelined/sync/test/src/SyncTests.scala	
@@ -1,5 +1,5 @@
 package sync
-import utest.*
+import utest._
 object SyncTests extends TestSuite:
   val tests = Tests:
     test("success"):
```
## Downstream Examples

- [17.4 - Deletes](https://github.com/handsonscala/handsonscala/tree/v2/examples/17.4%20-%20Deletes)
- [17.5 - Gzip](https://github.com/handsonscala/handsonscala/tree/v2/examples/17.5%20-%20Gzip)
- [17.6 - Ssh](https://github.com/handsonscala/handsonscala/tree/v2/examples/17.6%20-%20Ssh)