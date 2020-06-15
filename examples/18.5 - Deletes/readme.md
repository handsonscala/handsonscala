# Example 18.5 - Deletes
Real-time file synchronizer supporting deletion of files

```bash
./mill -i sync.test
```

## Upstream Example: [18.2 - Pipelined](https://github.com/handsonscala/handsonscala/tree/v1/examples/18.2%20-%20Pipelined):
Diff:
```diff
diff --git a/18.2 - Pipelined/agent/src/Agent.scala b/18.5 - Deletes/agent/src/Agent.scala
index 1239562..0bcb4ae 100644
--- a/18.2 - Pipelined/agent/src/Agent.scala	
+++ b/18.5 - Deletes/agent/src/Agent.scala	
@@ -13,6 +13,8 @@ object Agent {
         case Rpc.WriteOver(bytes, path) =>
           os.remove.all(os.pwd / path)
           os.write.over(os.pwd / path, bytes, createFolders = true)
+
+        case Rpc.Delete(path) => os.remove.all(os.pwd / path)
       }
     } catch { case e: java.io.EOFException => System.exit(0) }
   }
diff --git a/18.2 - Pipelined/shared/src/Rpc.scala b/18.5 - Deletes/shared/src/Rpc.scala
index 41d2c78..5638e7d 100644
--- a/18.2 - Pipelined/shared/src/Rpc.scala	
+++ b/18.5 - Deletes/shared/src/Rpc.scala	
@@ -11,6 +11,9 @@ object Rpc{
   case class WriteOver(src: Array[Byte], path: os.SubPath) extends Rpc
   implicit val writeOverRw: ReadWriter[WriteOver] = macroRW
 
+  case class Delete(path: os.SubPath) extends Rpc
+  implicit val deleteRw: ReadWriter[Delete] = macroRW
+
   case class StatInfo(p: os.SubPath, fileHash: Option[Int])
   implicit val statInfoRw: ReadWriter[StatInfo] = macroRW
 
diff --git a/18.2 - Pipelined/sync/src/Sync.scala b/18.5 - Deletes/sync/src/Sync.scala
index c842c04..d07440c 100644
--- a/18.2 - Pipelined/sync/src/Sync.scala	
+++ b/18.5 - Deletes/sync/src/Sync.scala	
@@ -15,8 +15,12 @@ object Sync {
         msg match {
           case ChangedPath(value) => Shared.send(agent.stdin.data, Rpc.StatPath(value))
           case HashStatInfo(localHash, Rpc.StatInfo(p, remoteHash)) =>
-            if (localHash != remoteHash && localHash.isDefined) {
+            if (localHash != remoteHash) {
+              if (localHash.isDefined) {
                 Shared.send(agent.stdin.data, Rpc.WriteOver(os.read.bytes(src / p), p))
+              }else{
+                Shared.send(agent.stdin.data, Rpc.Delete(p))
+              }
             }
         }
       }
diff --git a/18.2 - Pipelined/sync/test/src/SyncTests.scala b/18.5 - Deletes/sync/test/src/SyncTests.scala
index 254a888..a7af0a1 100644
--- a/18.2 - Pipelined/sync/test/src/SyncTests.scala	
+++ b/18.5 - Deletes/sync/test/src/SyncTests.scala	
@@ -31,6 +31,26 @@ object SyncTests extends TestSuite{
       println("SECOND VALIDATION")
       assert(os.read(dest / "folder1" / "hello.txt") == "hello")
       assert(os.read(dest /  "folder1" / "nested" / "world.txt") == "WORLD")
+
+      println("DELETE FILE 1")
+      os.remove(src / "folder1" / "nested" / "world.txt")
+      Thread.sleep(1000)
+      assert(!os.exists(dest /  "folder1" / "nested" / "world.txt"))
+
+      println("DELETE FILE 2")
+      os.remove(dest / "folder1" / "hello.txt")
+      Thread.sleep(1000)
+      assert(!os.exists(dest / "folder1" / "hello.txt"))
+
+      println("RE-CREATE FILES")
+      os.write.over(src / "folder1" / "hello.txt", "hello")
+      os.write.over(src / "folder1" / "nested" / "world.txt", "WORLD")
+
+      Thread.sleep(1000)
+
+      println("SECOND VALIDATION")
+      assert(os.read(dest / "folder1" / "hello.txt") == "hello")
+      assert(os.read(dest /  "folder1" / "nested" / "world.txt") == "WORLD")
     }
   }
 }
```
