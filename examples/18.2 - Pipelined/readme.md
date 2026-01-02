# Example 18.2 - Pipelined
Pipelined real-time file synchronizer, allowing RPCs and hashing to take place
in parallel

```bash
./mill -i sync.test
```

## Upstream Example: [18.1 - Simple](https://github.com/handsonscala/handsonscala/tree/v2/examples/18.1%20-%20Simple):
Diff:
```diff
diff --git a/18.1 - Simple/sync/src/Sync.scala b/18.2 - Pipelined/sync/src/Sync.scala
index 6dfbbad..81050bc 100644
--- a/18.1 - Simple/sync/src/Sync.scala	
+++ b/18.2 - Pipelined/sync/src/Sync.scala	
@@ -8,20 +8,25 @@ object Sync:
 
     enum Msg:
       case ChangedPath(value: os.SubPath)
-      case AgentResponse(value: Rpc.StatInfo)
+      case HashStatInfo(localHash: Option[Int], value: Rpc.StatInfo)
 
     import castor.Context.Simple.global
     object SyncActor extends castor.SimpleActor[Msg]:
       def run(msg: Msg): Unit = msg match
         case Msg.ChangedPath(value) => Shared.send(agent.stdin.data, Rpc.StatPath(value))
-        case Msg.AgentResponse(Rpc.StatInfo(p, remoteHash)) =>
-          val localHash = Shared.hashPath(src / p)
+        case Msg.HashStatInfo(localHash, Rpc.StatInfo(p, remoteHash)) =>
           if localHash != remoteHash && localHash.isDefined then
             Shared.send(agent.stdin.data, Rpc.WriteOver(os.read.bytes(src / p), p))
 
+    object HashActor extends castor.SimpleActor[Rpc.StatInfo]:
+      def run(msg: Rpc.StatInfo): Unit =
+        println("HashActor handling: " + msg)
+        val localHash = Shared.hashPath(src / msg.p)
+        SyncActor.send(Msg.HashStatInfo(localHash, msg))
+
     val agentReader = Thread(() =>
       while agent.isAlive() do
-        SyncActor.send(Msg.AgentResponse(Shared.receive[Rpc.StatInfo](agent.stdout.data)))
+        HashActor.send(Shared.receive[Rpc.StatInfo](agent.stdout.data))
     )
     agentReader.start()
 
```
## Downstream Examples

- [18.3 - InitialFiles](https://github.com/handsonscala/handsonscala/tree/v2/examples/18.3%20-%20InitialFiles)
- [18.4 - ForkJoinHashing](https://github.com/handsonscala/handsonscala/tree/v2/examples/18.4%20-%20ForkJoinHashing)
- [18.5 - Deletes](https://github.com/handsonscala/handsonscala/tree/v2/examples/18.5%20-%20Deletes)
- [18.6 - VirtualFileSystem](https://github.com/handsonscala/handsonscala/tree/v2/examples/18.6%20-%20VirtualFileSystem)