# Example 18.6 - VirtualFileSystem
Real-time file synchronizer that keeps track of synced files, avoiding RPCs for
destination file hashes

```bash
./mill -i sync.test
```

## Upstream Example: [18.2 - Pipelined](https://github.com/handsonscala/handsonscala/tree/v2/examples/18.2%20-%20Pipelined):
Diff:
```diff
diff --git a/18.2 - Pipelined/agent/src/Agent.scala b/18.6 - VirtualFileSystem/agent/src/Agent.scala
index 639159f..80c8a7c 100644
--- a/18.2 - Pipelined/agent/src/Agent.scala	
+++ b/18.6 - VirtualFileSystem/agent/src/Agent.scala	
@@ -7,9 +7,6 @@ object Agent:
 
     while true do try
       Shared.receive[Rpc](input) match
-        case Rpc.StatPath(path) =>
-          Shared.send(output, Rpc.StatInfo(path, Shared.hashPath(os.pwd / path)))
-
         case Rpc.WriteOver(bytes, path) =>
           os.remove.all(os.pwd / path)
           os.write.over(os.pwd / path, bytes, createFolders = true)
diff --git a/18.2 - Pipelined/shared/src/Rpc.scala b/18.6 - VirtualFileSystem/shared/src/Rpc.scala
index 1580f21..b49f9c6 100644
--- a/18.2 - Pipelined/shared/src/Rpc.scala	
+++ b/18.6 - VirtualFileSystem/shared/src/Rpc.scala	
@@ -4,8 +4,4 @@ given subPathRw: upickle.ReadWriter[os.SubPath] =
   upickle.readwriter[String].bimap[os.SubPath](_.toString, os.SubPath(_))
 
 enum Rpc derives upickle.ReadWriter:
-  case StatPath(path: os.SubPath)
   case WriteOver(src: Array[Byte], path: os.SubPath)
-
-object Rpc:
-  case class StatInfo(p: os.SubPath, fileHash: Option[Int]) derives upickle.ReadWriter
diff --git a/18.2 - Pipelined/sync/src/Sync.scala b/18.6 - VirtualFileSystem/sync/src/Sync.scala
index 81050bc..24bfb62 100644
--- a/18.2 - Pipelined/sync/src/Sync.scala	
+++ b/18.6 - VirtualFileSystem/sync/src/Sync.scala	
@@ -6,32 +6,27 @@ object Sync:
     os.perms.set(agentExecutable, "rwx------")
     val agent = os.spawn(cmd = agentExecutable, cwd = dest)
 
-    enum Msg:
-      case ChangedPath(value: os.SubPath)
-      case HashStatInfo(localHash: Option[Int], value: Rpc.StatInfo)
+    case class HashStatInfo(localHash: Option[Int], path: os.SubPath)
 
     import castor.Context.Simple.global
-    object SyncActor extends castor.SimpleActor[Msg]:
-      def run(msg: Msg): Unit = msg match
-        case Msg.ChangedPath(value) => Shared.send(agent.stdin.data, Rpc.StatPath(value))
-        case Msg.HashStatInfo(localHash, Rpc.StatInfo(p, remoteHash)) =>
-          if localHash != remoteHash && localHash.isDefined then
-            Shared.send(agent.stdin.data, Rpc.WriteOver(os.read.bytes(src / p), p))
+    object SyncActor extends castor.SimpleActor[HashStatInfo]:
+      val fileHashMap = collection.mutable.Map.empty[os.SubPath, Int]
+      def run(msg: HashStatInfo): Unit =
+        if msg.localHash != fileHashMap.get(msg.path) then
+          msg.localHash match
+            case None => fileHashMap.remove(msg.path)
+            case Some(hash) => fileHashMap(msg.path) = hash
 
-    object HashActor extends castor.SimpleActor[Rpc.StatInfo]:
-      def run(msg: Rpc.StatInfo): Unit =
-        println("HashActor handling: " + msg)
-        val localHash = Shared.hashPath(src / msg.p)
-        SyncActor.send(Msg.HashStatInfo(localHash, msg))
+          if msg.localHash.isDefined then
+            Shared.send(agent.stdin.data, Rpc.WriteOver(os.read.bytes(src / msg.path), msg.path))
 
-    val agentReader = Thread(() =>
-      while agent.isAlive() do
-        HashActor.send(Shared.receive[Rpc.StatInfo](agent.stdout.data))
-    )
-    agentReader.start()
+    object HashActor extends castor.SimpleActor[os.SubPath]:
+      def run(path: os.SubPath): Unit =
+        val localHash = Shared.hashPath(src / path)
+        SyncActor.send(HashStatInfo(localHash, path))
 
     val watcher = os.watch.watch(
       Seq(src),
-      onEvent = _.foreach(p => SyncActor.send(Msg.ChangedPath(p.subRelativeTo(src))))
+      onEvent = _.foreach(p => HashActor.send(p.subRelativeTo(src)))
     )
     Thread.sleep(Long.MaxValue)
```
