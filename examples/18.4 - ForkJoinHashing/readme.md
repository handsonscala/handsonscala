# Example 18.4 - ForkJoinHashing
Real-time file synchronizer that does hashing of files in parallel

```bash
./mill -i sync.test
```

## Upstream Example: [18.2 - Pipelined](https://github.com/handsonscala/handsonscala/tree/v2/examples/18.2%20-%20Pipelined):
Diff:
```diff
diff --git a/18.2 - Pipelined/sync/src/Sync.scala b/18.4 - ForkJoinHashing/sync/src/Sync.scala
index 81050bc..be865c5 100644
--- a/18.2 - Pipelined/sync/src/Sync.scala	
+++ b/18.4 - ForkJoinHashing/sync/src/Sync.scala	
@@ -1,4 +1,5 @@
 package sync
+import scala.concurrent.Future
 object Sync:
   def main(src0: String, dest0: String): Unit =
     val (src, dest) = (os.Path(src0, os.pwd), os.Path(dest0, os.pwd))
@@ -18,15 +19,34 @@ object Sync:
           if localHash != remoteHash && localHash.isDefined then
             Shared.send(agent.stdin.data, Rpc.WriteOver(os.read.bytes(src / p), p))
 
-    object HashActor extends castor.SimpleActor[Rpc.StatInfo]:
-      def run(msg: Rpc.StatInfo): Unit =
-        println("HashActor handling: " + msg)
-        val localHash = Shared.hashPath(src / msg.p)
-        SyncActor.send(Msg.HashStatInfo(localHash, msg))
+    enum HashActorMsg:
+      case SingleStatInfo(value: Rpc.StatInfo)
+      case HashComplete(values: Seq[Msg.HashStatInfo])
+
+    object HashActor extends castor.StateMachineActor[HashActorMsg]:
+      def initialState = Idle()
+      case class Buffering(msgs: Map[os.SubPath, Option[Int]]) extends State({
+        case HashActorMsg.SingleStatInfo(value) => Buffering(msgs + (value.p -> value.fileHash))
+        case HashActorMsg.HashComplete(values) =>
+          values.foreach(SyncActor.send(_))
+          if msgs.isEmpty then Idle()
+          else processBuffered(msgs)
+      })
+
+      case class Idle() extends State({
+        case HashActorMsg.SingleStatInfo(statInfo) => processBuffered(Map(statInfo.p -> statInfo.fileHash))
+      })
+
+      def processBuffered(msgs: Map[os.SubPath, Option[Int]]) =
+        val futures = for (p, fileHash) <- msgs yield Future[Msg.HashStatInfo]:
+          Msg.HashStatInfo(Shared.hashPath(src / p), Rpc.StatInfo(p, fileHash))
+
+        this.sendAsync(Future.sequence(futures.toSeq).map(HashActorMsg.HashComplete(_)))
+        Buffering(Map())
 
     val agentReader = Thread(() =>
       while agent.isAlive() do
-        HashActor.send(Shared.receive[Rpc.StatInfo](agent.stdout.data))
+        HashActor.send(HashActorMsg.SingleStatInfo(Shared.receive[Rpc.StatInfo](agent.stdout.data)))
     )
     agentReader.start()
 
```
