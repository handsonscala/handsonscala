# Example 18.6 - VirtualFileSystem
Real-time file synchronizer that keeps track of synced files, avoiding RPCs for
destination file hashes

```bash
./mill -i sync.test
```

## Upstream Example: [18.2 - Pipelined](https://github.com/handsonscala/handsonscala/tree/v1/examples/18.2%20-%20Pipelined):
Diff:
```diff
diff --git a/18.2 - Pipelined/agent/src/Agent.scala b/18.6 - VirtualFileSystem/agent/src/Agent.scala
index 1239562..ffe1a3d 100644
--- a/18.2 - Pipelined/agent/src/Agent.scala	
+++ b/18.6 - VirtualFileSystem/agent/src/Agent.scala	
@@ -7,9 +7,6 @@ object Agent {
       val rpc = Shared.receive[Rpc](input)
       System.err.println("Agent handling: " + rpc)
       rpc match {
-        case Rpc.StatPath(path) =>
-          Shared.send(output, Rpc.StatInfo(path, Shared.hashPath(os.pwd / path)))
-
         case Rpc.WriteOver(bytes, path) =>
           os.remove.all(os.pwd / path)
           os.write.over(os.pwd / path, bytes, createFolders = true)
diff --git a/18.2 - Pipelined/shared/src/Rpc.scala b/18.6 - VirtualFileSystem/shared/src/Rpc.scala
index 41d2c78..cdefe20 100644
--- a/18.2 - Pipelined/shared/src/Rpc.scala	
+++ b/18.6 - VirtualFileSystem/shared/src/Rpc.scala	
@@ -5,14 +5,8 @@ sealed trait Rpc
 object Rpc{
   implicit val subPathRw = readwriter[String].bimap[os.SubPath](_.toString, os.SubPath(_))
 
-  case class StatPath(path: os.SubPath) extends Rpc
-  implicit val statPathRw: ReadWriter[StatPath] = macroRW
-
   case class WriteOver(src: Array[Byte], path: os.SubPath) extends Rpc
   implicit val writeOverRw: ReadWriter[WriteOver] = macroRW
 
-  case class StatInfo(p: os.SubPath, fileHash: Option[Int])
-  implicit val statInfoRw: ReadWriter[StatInfo] = macroRW
-
   implicit val msgRw: ReadWriter[Rpc] = macroRW
 }
diff --git a/18.2 - Pipelined/sync/src/Sync.scala b/18.6 - VirtualFileSystem/sync/src/Sync.scala
index c842c04..67ade64 100644
--- a/18.2 - Pipelined/sync/src/Sync.scala	
+++ b/18.6 - VirtualFileSystem/sync/src/Sync.scala	
@@ -5,38 +5,33 @@ object Sync {
     val agentExecutable = os.temp(os.read.bytes(os.resource / "agent.jar"))
     os.perms.set(agentExecutable, "rwx------")
     val agent = os.proc(agentExecutable).spawn(cwd = dest)
-    sealed trait Msg
-    case class ChangedPath(value: os.SubPath) extends Msg
-    case class HashStatInfo(localHash: Option[Int], value: Rpc.StatInfo) extends Msg
+    case class HashStatInfo(localHash: Option[Int], path: os.SubPath)
     import castor.Context.Simple.global
-    object SyncActor extends castor.SimpleActor[Msg]{
-      def run(msg: Msg): Unit = {
+    object SyncActor extends castor.SimpleActor[HashStatInfo]{
+      val fileHashMap = collection.mutable.Map.empty[os.SubPath, Int]
+      def run(msg: HashStatInfo): Unit = {
         println("SyncActor handling: " + msg)
-        msg match {
-          case ChangedPath(value) => Shared.send(agent.stdin.data, Rpc.StatPath(value))
-          case HashStatInfo(localHash, Rpc.StatInfo(p, remoteHash)) =>
-            if (localHash != remoteHash && localHash.isDefined) {
-              Shared.send(agent.stdin.data, Rpc.WriteOver(os.read.bytes(src / p), p))
+        if (msg.localHash != fileHashMap.get(msg.path)) {
+          msg.localHash match{
+            case None => fileHashMap.remove(msg.path)
+            case Some(hash) => fileHashMap(msg.path) = hash
           }
+          if (msg.localHash.isDefined){
+            Shared.send(agent.stdin.data, Rpc.WriteOver(os.read.bytes(src / msg.path), msg.path))
           }
         }
       }
-    object HashActor extends castor.SimpleActor[Rpc.StatInfo]{
-      def run(msg: Rpc.StatInfo): Unit = {
-        println("HashActor handling: " + msg)
-        val localHash = Shared.hashPath(src / msg.p)
-        SyncActor.send(HashStatInfo(localHash, msg))
     }
+    object HashActor extends castor.SimpleActor[os.SubPath]{
+      def run(path: os.SubPath): Unit = {
+        println("HashActor handling: " + path)
+        val localHash = Shared.hashPath(src / path)
+        SyncActor.send(HashStatInfo(localHash, path))
       }
-    val agentReader = new Thread(() => {
-      while (agent.isAlive()) {
-        HashActor.send(Shared.receive[Rpc.StatInfo](agent.stdout.data))
     }
-    })
-    agentReader.start()
     val watcher = os.watch.watch(
       Seq(src),
-      onEvent = _.foreach(p => SyncActor.send(ChangedPath(p.subRelativeTo(src))))
+      onEvent = _.foreach(p => HashActor.send(p.subRelativeTo(src)))
     )
     Thread.sleep(Long.MaxValue)
   }
```
