# Example 17.5 - Gzip
File syncer with compressed data exchange between the Sync and Agent processes

```bash
./mill -i sync.test
```

## Upstream Example: [17.3 - Pipelined](https://github.com/handsonscala/handsonscala/tree/v2/examples/17.3%20-%20Pipelined):
Diff:
```diff
diff --git a/17.3 - Pipelined/agent/src/Agent.scala b/17.5 - Gzip/agent/src/Agent.scala
index b0936dd..837ec0d 100644
--- a/17.3 - Pipelined/agent/src/Agent.scala	
+++ b/17.5 - Gzip/agent/src/Agent.scala	
@@ -2,8 +2,8 @@ package sync
 
 object Agent:
   @main def run(): Unit =
-    val input = java.io.DataInputStream(System.in)
-    val output = java.io.DataOutputStream(System.out)
+    val input = java.io.DataInputStream(java.util.zip.GZIPInputStream(System.in))
+    val output = java.io.DataOutputStream(java.util.zip.GZIPOutputStream(System.out, true))
 
     while true do try
       Shared.receive[Rpc](input) match
diff --git a/17.3 - Pipelined/sync/src/Sync.scala b/17.5 - Gzip/sync/src/Sync.scala
index c9514ed..d991b37 100644
--- a/17.3 - Pipelined/sync/src/Sync.scala	
+++ b/17.5 - Gzip/sync/src/Sync.scala	
@@ -7,12 +7,15 @@ object Sync:
     val agentExecutable = os.temp(os.read.bytes(os.resource / "agent.jar"))
     os.perms.set(agentExecutable, "rwx------")
     val agent = os.spawn(cmd = agentExecutable, cwd = dest)
+    val agentIn = java.io.DataOutputStream(java.util.zip.GZIPOutputStream(agent.stdin, true))
+    // Use `lazy val` to defer construction of GZIPInputStream
+    // until the agent has begun senging back data
+    lazy val agentOut = java.io.DataInputStream(java.util.zip.GZIPInputStream(agent.stdout))
     def callAgent[T: upickle.Reader](rpc: Rpc): () => T =
-      Shared.send(agent.stdin.data, rpc)
-      () => Shared.receive[T](agent.stdout.data)
+      Shared.send(agentIn, rpc)
+      () => Shared.receive[T](agentOut)
 
     val subPaths = os.walk(src).map(_.subRelativeTo(src))
-
     def pipelineCalls[T: upickle.Reader](rpcFor: os.SubPath => Option[Rpc]) =
       val buffer = collection.mutable.Buffer.empty[(os.RelPath, () => T)]
       for p <- subPaths; rpc <- rpcFor(p) do buffer.addOne((p, callAgent[T](rpc)))
@@ -20,7 +23,6 @@ object Sync:
 
     val existsMap = pipelineCalls[Boolean](p => Some(Rpc.Exists(p)))
     val isDirMap = pipelineCalls[Boolean](p => Some(Rpc.IsDir(p)))
-
     val readMap = pipelineCalls[Array[Byte]]: p =>
       if existsMap(p) && !isDirMap(p) then Some(Rpc.ReadBytes(p))
       else None
```
