# Example 17.3 - Pipelined
Pipelined version of our two-process file synchronizer, minimizing the
chattiness of the protocol

```bash
./mill -i sync.test
```

## Upstream Example: [17.2 - FileSyncer](https://github.com/handsonscala/handsonscala/tree/v1/examples/17.2%20-%20FileSyncer):
Diff:
```diff
diff --git a/17.2 - FileSyncer/sync/src/Sync.scala b/17.3 - Pipelined/sync/src/Sync.scala
index 30e1a48..367c163 100644
--- a/17.2 - FileSyncer/sync/src/Sync.scala	
+++ b/17.3 - Pipelined/sync/src/Sync.scala	
@@ -6,29 +6,28 @@ object Sync {
     val agentExecutable = os.temp(os.read.bytes(os.resource / "agent.jar"))
     os.perms.set(agentExecutable, "rwx------")
     val agent = os.proc(agentExecutable).spawn(cwd = dest)
-    def callAgent[T: upickle.default.Reader](rpc: Rpc): T = {
+    def callAgent[T: upickle.default.Reader](rpc: Rpc): () => T = {
       Shared.send(agent.stdin.data, rpc)
-      Shared.receive[T](agent.stdout.data)
+      () => Shared.receive[T](agent.stdout.data)
     }
-    for (srcSubPath <- os.walk(src)) {
-      val subPath = srcSubPath.subRelativeTo(src)
-      val destSubPath = dest / subPath
-      (os.isDir(srcSubPath), callAgent[Boolean](Rpc.IsDir(subPath))) match {
-        case (false, true) =>
-          callAgent[Unit](Rpc.WriteOver(os.read.bytes(srcSubPath), subPath))
-        case (true, false) =>
-          for (p <- os.walk(srcSubPath) if os.isFile(p)) {
-            callAgent[Unit](Rpc.WriteOver(os.read.bytes(p), p.subRelativeTo(src)))
+    val subPaths = os.walk(src).map(_.subRelativeTo(src))
+    def pipelineCalls[T: upickle.default.Reader](rpcFor: os.SubPath => Option[Rpc]) = {
+      val buffer = collection.mutable.Buffer.empty[(os.RelPath, () => T)]
+      for (p <- subPaths; rpc <- rpcFor (p)) buffer.append((p, callAgent[T](rpc)))
+      buffer.map{case (k, v) => (k, v())}.toMap
     }
-        case (false, false)
-          if !callAgent[Boolean](Rpc.Exists(subPath))
-            || !os.read.bytes(srcSubPath).sameElements(
-            callAgent[Array[Byte]](Rpc.ReadBytes(subPath))
-          ) =>
-
-          callAgent[Unit](Rpc.WriteOver(os.read.bytes(srcSubPath), subPath))
-
-        case _ => // do nothing
+    val existsMap = pipelineCalls[Boolean](p => Some(Rpc.Exists(p)))
+    val isDirMap = pipelineCalls[Boolean](p => Some(Rpc.IsDir(p)))
+    val readMap = pipelineCalls[Array[Byte]]{p =>
+      if (existsMap(p) && !isDirMap(p)) Some(Rpc.ReadBytes(p))
+      else None
+    }
+    pipelineCalls[Unit]{ p =>
+      if (os.isDir(src / p)) None
+      else {
+        val localBytes = os.read.bytes(src / p)
+        if (readMap.get(p).exists(java.util.Arrays.equals(_, localBytes))) None
+        else Some(Rpc.WriteOver(localBytes, p))
       }
     }
   }
```
## Downstream Examples

- [17.4 - Deletes](https://github.com/handsonscala/handsonscala/tree/v1/examples/17.4%20-%20Deletes)