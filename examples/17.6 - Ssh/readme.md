# Example 17.6 - Ssh
Networked of our two-process file synchronizer, running the agent on a separate
computer and interacting with it over SSH

```bash
./mill -i sync.test
```

## Upstream Example: [17.3 - Pipelined](https://github.com/handsonscala/handsonscala/tree/v1/examples/17.3%20-%20Pipelined):
Diff:
```diff
diff --git a/17.3 - Pipelined/sync/src/Sync.scala b/17.6 - Ssh/sync/src/Sync.scala
index 367c163..cc3d038 100644
--- a/17.3 - Pipelined/sync/src/Sync.scala	
+++ b/17.6 - Ssh/sync/src/Sync.scala	
@@ -2,10 +2,13 @@ package sync
 object Sync {
   def main(args: Array[String]): Unit = {
     val src = os.Path(args(0), os.pwd)
-    val dest = os.Path(args(1), os.pwd)
+    val dest = os.RelPath(args(1))
+    val remote = args(2)
     val agentExecutable = os.temp(os.read.bytes(os.resource / "agent.jar"))
-    os.perms.set(agentExecutable, "rwx------")
-    val agent = os.proc(agentExecutable).spawn(cwd = dest)
+    os.proc("scp", agentExecutable, s"$remote:/home/ubuntu/agent.jar").call()
+    os.proc("ssh", remote, "chmod", "+x", "/home/ubuntu/agent.jar").call()
+    os.proc("ssh", remote, "mkdir", "-p", dest).call()
+    val agent = os.proc("ssh", remote, s"cd $dest; /home/ubuntu/agent.jar").spawn()
     def callAgent[T: upickle.default.Reader](rpc: Rpc): () => T = {
       Shared.send(agent.stdin.data, rpc)
       () => Shared.receive[T](agent.stdout.data)
diff --git a/17.3 - Pipelined/sync/test/src/SyncTests.scala b/17.6 - Ssh/sync/test/src/SyncTests.scala
index 1582ddf..96c3f99 100644
--- a/17.3 - Pipelined/sync/test/src/SyncTests.scala	
+++ b/17.6 - Ssh/sync/test/src/SyncTests.scala	
@@ -1,33 +1,37 @@
 package sync
 import utest._
 object SyncTests extends TestSuite{
+  val remote = "ubuntu@54.153.33.113"
   val tests = Tests{
+    def readRemote(p: os.RelPath) = {
+      os.proc("ssh", remote, "cat", p.toString).call().out.text()
+    }
     test("success"){
 
       println("INITIALIZING SRC AND DEST")
       val src = os.temp.dir(os.pwd / "out")
-      val dest = os.temp.dir(os.pwd / "out")
+      val dest = os.rel / "out" / "dest"
 
       os.write(src / "folder1" / "hello.txt", "HELLO", createFolders = true)
       os.write(src / "folder1" / "nested" / "world.txt", "world", createFolders = true)
 
       println("FIRST SYNC")
-      Sync.main(Array(src.toString, dest.toString))
+      Sync.main(Array(src.toString, dest.toString, remote))
 
       println("FIRST VALIDATION")
-      assert(os.read(dest / "folder1" / "hello.txt") == "HELLO")
-      assert(os.read(dest /  "folder1" / "nested" / "world.txt") == "world")
+      assert(readRemote(dest / "folder1" / "hello.txt") == "HELLO")
+      assert(readRemote(dest /  "folder1" / "nested" / "world.txt") == "world")
 
       println("UPDATE SRC")
       os.write.over(src / "folder1" / "hello.txt", "hello")
       os.write.over(src / "folder1" / "nested" / "world.txt", "WORLD")
 
       println("SECOND SYNC")
-      Sync.main(Array(src.toString, dest.toString))
+      Sync.main(Array(src.toString, dest.toString, remote))
 
       println("SECOND VALIDATION")
-      assert(os.read(dest / "folder1" / "hello.txt") == "hello")
-      assert(os.read(dest /  "folder1" / "nested" / "world.txt") == "WORLD")
+      assert(readRemote(dest / "folder1" / "hello.txt") == "hello")
+      assert(readRemote(dest /  "folder1" / "nested" / "world.txt") == "WORLD")
     }
   }
 }
```
