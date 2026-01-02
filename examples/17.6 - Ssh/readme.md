# Example 17.6 - Ssh
Networked of our two-process file synchronizer, running the agent on a separate
computer and interacting with it over SSH

```bash
./mill -i sync.test
```

## Upstream Example: [17.3 - Pipelined](https://github.com/handsonscala/handsonscala/tree/v2/examples/17.3%20-%20Pipelined):
Diff:
```diff
diff --git a/17.3 - Pipelined/sync/src/Sync.scala b/17.6 - Ssh/sync/src/Sync.scala
index c9514ed..4d4ef1d 100644
--- a/17.3 - Pipelined/sync/src/Sync.scala	
+++ b/17.6 - Ssh/sync/src/Sync.scala	
@@ -1,18 +1,19 @@
 package sync
 object Sync:
-  def main(src0: String, dest0: String): Unit =
+  def main(src0: String, dest0: String, remote: String): Unit =
     val src = os.Path(src0, os.pwd)
-    val dest = os.Path(dest0, os.pwd)
+    val dest = os.RelPath(dest0)
 
     val agentExecutable = os.temp(os.read.bytes(os.resource / "agent.jar"))
-    os.perms.set(agentExecutable, "rwx------")
-    val agent = os.spawn(cmd = agentExecutable, cwd = dest)
+    os.call(cmd = ("scp", agentExecutable, s"$remote:~/agent.jar"))
+    os.call(cmd = ("ssh", remote, "chmod", "+x", "~/agent.jar"))
+    os.call(cmd = ("ssh", remote, "mkdir", "-p", dest))
+    val agent = os.spawn(cmd = ("ssh", remote, s"cd $dest; ~/agent.jar"))
     def callAgent[T: upickle.Reader](rpc: Rpc): () => T =
       Shared.send(agent.stdin.data, rpc)
       () => Shared.receive[T](agent.stdout.data)
 
     val subPaths = os.walk(src).map(_.subRelativeTo(src))
-
     def pipelineCalls[T: upickle.Reader](rpcFor: os.SubPath => Option[Rpc]) =
       val buffer = collection.mutable.Buffer.empty[(os.RelPath, () => T)]
       for p <- subPaths; rpc <- rpcFor(p) do buffer.addOne((p, callAgent[T](rpc)))
@@ -20,7 +21,6 @@ object Sync:
 
     val existsMap = pipelineCalls[Boolean](p => Some(Rpc.Exists(p)))
     val isDirMap = pipelineCalls[Boolean](p => Some(Rpc.IsDir(p)))
-
     val readMap = pipelineCalls[Array[Byte]]: p =>
       if existsMap(p) && !isDirMap(p) then Some(Rpc.ReadBytes(p))
       else None
diff --git a/17.3 - Pipelined/sync/test/src/SyncTests.scala b/17.6 - Ssh/sync/test/src/SyncTests.scala
index 3ebacd0..e69f79e 100644
--- a/17.3 - Pipelined/sync/test/src/SyncTests.scala	
+++ b/17.6 - Ssh/sync/test/src/SyncTests.scala	
@@ -1,30 +1,33 @@
 package sync
 import utest._
 object SyncTests extends TestSuite:
+  val remote = "ec2-user@18.143.166.245"
   val tests = Tests:
+    def readRemote(p: os.RelPath) =
+      os.call(cmd = ("ssh", remote, "cat", p.toString)).out.text()
     test("success"):
 
       println("INITIALIZING SRC AND DEST")
       val src = os.temp.dir(os.pwd)
-      val dest = os.temp.dir(os.pwd)
+      val dest = os.rel / "out/dest"
 
       os.write(src / "folder1/hello.txt", "HELLO", createFolders = true)
       os.write(src / "folder1/nested/world.txt", "world", createFolders = true)
 
       println("FIRST SYNC")
-      Sync.main(src.toString, dest.toString)
+      Sync.main(src.toString, dest.toString, remote)
 
       println("FIRST VALIDATION")
-      assert(os.read(dest / "folder1/hello.txt") == "HELLO")
-      assert(os.read(dest /  "folder1/nested/world.txt") == "world")
+      assert(readRemote(dest / "folder1/hello.txt") == "HELLO")
+      assert(readRemote(dest /  "folder1/nested/world.txt") == "world")
 
       println("UPDATE SRC")
       os.write.over(src / "folder1/hello.txt", "hello")
       os.write.over(src / "folder1/nested/world.txt", "WORLD")
 
       println("SECOND SYNC")
-      Sync.main(src.toString, dest.toString)
+      Sync.main(src.toString, dest.toString, remote)
 
       println("SECOND VALIDATION")
-      assert(os.read(dest / "folder1/hello.txt") == "hello")
-      assert(os.read(dest /  "folder1/nested/world.txt") == "WORLD")
+      assert(readRemote(dest / "folder1/hello.txt") == "hello")
+      assert(readRemote(dest /  "folder1/nested/world.txt") == "WORLD")
```
