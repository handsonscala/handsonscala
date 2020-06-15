# Example 18.4 - ForkJoinHashing
Real-time file synchronizer that does hashing of files in parallel

```bash
./mill -i sync.test
```

## Upstream Example: [18.2 - Pipelined](https://github.com/handsonscala/handsonscala/tree/v1/examples/18.2%20-%20Pipelined):
Diff:
```diff
diff --git a/18.2 - Pipelined/sync/src/Sync.scala b/18.4 - ForkJoinHashing/sync/src/Sync.scala
index c842c04..2aa7051 100644
--- a/18.2 - Pipelined/sync/src/Sync.scala	
+++ b/18.4 - ForkJoinHashing/sync/src/Sync.scala	
@@ -1,4 +1,5 @@
 package sync
+import scala.concurrent.Future
 object Sync {
   def main(args: Array[String]): Unit = {
     val (src, dest) = (os.Path(args(0), os.pwd), os.Path(args(1), os.pwd))
@@ -21,16 +22,33 @@ object Sync {
         }
       }
     }
-    object HashActor extends castor.SimpleActor[Rpc.StatInfo]{
-      def run(msg: Rpc.StatInfo): Unit = {
-        println("HashActor handling: " + msg)
-        val localHash = Shared.hashPath(src / msg.p)
-        SyncActor.send(HashStatInfo(localHash, msg))
+    sealed trait HashActorMsg
+    case class SingleStatInfo(value: Rpc.StatInfo) extends HashActorMsg
+    case class HashComplete(values: Seq[HashStatInfo]) extends HashActorMsg
+    object HashActor extends castor.StateMachineActor[HashActorMsg]{
+      def initialState = Idle()
+      case class Buffering(msgs: Map[os.SubPath, Option[Int]]) extends State({
+        case SingleStatInfo(value) => Buffering(msgs + (value.p -> value.fileHash))
+        case HashComplete(values) =>
+          values.foreach(SyncActor.send(_))
+          if (msgs.isEmpty) Idle()
+          else processBuffered(msgs)
+      })
+      case class Idle() extends State({
+        case SingleStatInfo(statInfo) => processBuffered(Map(statInfo.p -> statInfo.fileHash))
+      })
+      def processBuffered(msgs: Map[os.SubPath, Option[Int]]) = {
+        val futures = for((p, fileHash) <- msgs) yield Future {
+          HashStatInfo(Shared.hashPath(src / p), Rpc.StatInfo(p, fileHash))
+        }
+
+        this.sendAsync(Future.sequence(futures.toSeq).map(HashComplete(_)))
+        Buffering(Map())
       }
     }
     val agentReader = new Thread(() => {
       while (agent.isAlive()) {
-        HashActor.send(Shared.receive[Rpc.StatInfo](agent.stdout.data))
+        HashActor.send(SingleStatInfo(Shared.receive[Rpc.StatInfo](agent.stdout.data)))
       }
     })
     agentReader.start()
```
