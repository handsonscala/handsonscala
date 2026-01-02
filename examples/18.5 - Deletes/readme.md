# Example 18.5 - Deletes
Real-time file synchronizer supporting deletion of files

```bash
./mill -i sync.test
```

## Upstream Example: [18.2 - Pipelined](https://github.com/handsonscala/handsonscala/tree/v2/examples/18.2%20-%20Pipelined):
Diff:
```diff
diff --git a/18.2 - Pipelined/agent/src/Agent.scala b/18.5 - Deletes/agent/src/Agent.scala
index 639159f..4e91c7b 100644
--- a/18.2 - Pipelined/agent/src/Agent.scala	
+++ b/18.5 - Deletes/agent/src/Agent.scala	
@@ -14,4 +14,6 @@ object Agent:
           os.remove.all(os.pwd / path)
           os.write.over(os.pwd / path, bytes, createFolders = true)
 
+        case Rpc.Delete(path) => os.remove.all(os.pwd / path)
+
     catch case e: java.io.EOFException => System.exit(0)
diff --git a/18.2 - Pipelined/shared/src/Rpc.scala b/18.5 - Deletes/shared/src/Rpc.scala
index 1580f21..00a04c6 100644
--- a/18.2 - Pipelined/shared/src/Rpc.scala	
+++ b/18.5 - Deletes/shared/src/Rpc.scala	
@@ -6,6 +6,7 @@ given subPathRw: upickle.ReadWriter[os.SubPath] =
 enum Rpc derives upickle.ReadWriter:
   case StatPath(path: os.SubPath)
   case WriteOver(src: Array[Byte], path: os.SubPath)
+  case Delete(path: os.SubPath)
 
 object Rpc:
   case class StatInfo(p: os.SubPath, fileHash: Option[Int]) derives upickle.ReadWriter
diff --git a/18.2 - Pipelined/sync/src/Sync.scala b/18.5 - Deletes/sync/src/Sync.scala
index 81050bc..d090334 100644
--- a/18.2 - Pipelined/sync/src/Sync.scala	
+++ b/18.5 - Deletes/sync/src/Sync.scala	
@@ -15,12 +15,14 @@ object Sync:
       def run(msg: Msg): Unit = msg match
         case Msg.ChangedPath(value) => Shared.send(agent.stdin.data, Rpc.StatPath(value))
         case Msg.HashStatInfo(localHash, Rpc.StatInfo(p, remoteHash)) =>
-          if localHash != remoteHash && localHash.isDefined then
+          if localHash != remoteHash then
+            if localHash.isDefined then
               Shared.send(agent.stdin.data, Rpc.WriteOver(os.read.bytes(src / p), p))
+            else
+              Shared.send(agent.stdin.data, Rpc.Delete(p))
 
     object HashActor extends castor.SimpleActor[Rpc.StatInfo]:
       def run(msg: Rpc.StatInfo): Unit =
-        println("HashActor handling: " + msg)
         val localHash = Shared.hashPath(src / msg.p)
         SyncActor.send(Msg.HashStatInfo(localHash, msg))
 
diff --git a/18.2 - Pipelined/sync/test/src/SyncTests.scala b/18.5 - Deletes/sync/test/src/SyncTests.scala
index 41d9d68..359da3e 100644
--- a/18.2 - Pipelined/sync/test/src/SyncTests.scala	
+++ b/18.5 - Deletes/sync/test/src/SyncTests.scala	
@@ -31,3 +31,23 @@ object SyncTests extends TestSuite:
       println("SECOND VALIDATION")
       assert(os.read(dest / "folder1/hello.txt") == "hello")
       assert(os.read(dest /  "folder1/nested/world.txt") == "WORLD")
+
+      println("DELETE FILE 1")
+      os.remove(src / "folder1/nested/world.txt")
+      Thread.sleep(1000)
+      assert(!os.exists(dest /  "folder1/nested/world.txt"))
+
+      println("DELETE FILE 2")
+      os.remove(dest / "folder1/hello.txt")
+      Thread.sleep(1000)
+      assert(!os.exists(dest / "folder1/hello.txt"))
+
+      println("RE-CREATE FILES")
+      os.write.over(src / "folder1/hello.txt", "hello")
+      os.write.over(src / "folder1/nested/world.txt", "WORLD")
+
+      Thread.sleep(1000)
+
+      println("SECOND VALIDATION")
+      assert(os.read(dest / "folder1/hello.txt") == "hello")
+      assert(os.read(dest /  "folder1/nested/world.txt") == "WORLD")
```
