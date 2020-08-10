# Example 17.5 - Gzip
File syncer with compressed data exchange between the Sync and Agent processes

```bash
./mill -i sync.test
```

## Upstream Example: [17.3 - Pipelined](https://github.com/handsonscala/handsonscala/tree/v1/examples/17.3%20-%20Pipelined):
Diff:
```diff
diff --git a/17.3 - Pipelined/agent/src/Agent.scala b/17.5 - Gzip/agent/src/Agent.scala
index a7d17cc..f5e8e4b 100644
--- a/17.3 - Pipelined/agent/src/Agent.scala	
+++ b/17.5 - Gzip/agent/src/Agent.scala	
@@ -1,8 +1,8 @@
 package sync
 object Agent {
   def main(args: Array[String]): Unit = {
-    val input = new java.io.DataInputStream(System.in)
-    val output = new java.io.DataOutputStream(System.out)
+    val input = new java.io.DataInputStream(new java.util.zip.GZIPInputStream(System.in))
+    val output = new java.io.DataOutputStream(new java.util.zip.GZIPOutputStream(System.out, true))
     while (true) try {
       val rpc = Shared.receive[Rpc](input)
       rpc match {
diff --git a/17.3 - Pipelined/sync/src/Sync.scala b/17.5 - Gzip/sync/src/Sync.scala
index 367c163..4a5c646 100644
--- a/17.3 - Pipelined/sync/src/Sync.scala	
+++ b/17.5 - Gzip/sync/src/Sync.scala	
@@ -6,9 +6,13 @@ object Sync {
     val agentExecutable = os.temp(os.read.bytes(os.resource / "agent.jar"))
     os.perms.set(agentExecutable, "rwx------")
     val agent = os.proc(agentExecutable).spawn(cwd = dest)
+    val agentIn = new java.io.DataOutputStream(new java.util.zip.GZIPOutputStream(agent.stdin, true))
+    // Use `lazy val` to defer construction of GZIPInputStream
+    // until the agent has begun senging back data
+    lazy val agentOut = new java.io.DataInputStream(new java.util.zip.GZIPInputStream(agent.stdout))
     def callAgent[T: upickle.default.Reader](rpc: Rpc): () => T = {
-      Shared.send(agent.stdin.data, rpc)
-      () => Shared.receive[T](agent.stdout.data)
+      Shared.send(agentIn, rpc)
+      () => Shared.receive[T](agentOut)
     }
     val subPaths = os.walk(src).map(_.subRelativeTo(src))
     def pipelineCalls[T: upickle.default.Reader](rpcFor: os.SubPath => Option[Rpc]) = {
```
