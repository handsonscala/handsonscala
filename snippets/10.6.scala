 import mill.*
 def srcs = Task.Source("src")
+def resources = Task.Source("resources")

 def concat = Task: ...

+def compress = Task:
+  for p <- os.list(resources().path) do
+    val copied = Task.dest / p.relativeTo(resources().path)
+    os.copy(p, copied)
+    os.call(cmd = ("gzip", copied))
+
+  PathRef(Task.dest)
+
+def zipped = Task:
+  val temp = Task.dest / "temp"
+  os.makeDir(temp)
+  os.copy(concat().path, temp / "concat.txt")
+
+  for p <- os.list(compress().path) do
+    os.copy(p, temp / p.relativeTo(compress().path))
+
+  os.call(cmd = ("zip", "-r", Task.dest / "out.zip", "."), cwd = temp)
+  PathRef(Task.dest / "out.zip")
+