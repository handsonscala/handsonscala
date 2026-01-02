# Example 10.2 - Nonlinear
Simple non-linear build pipeline with two branches

```bash
./mill -i zipped
```

## Upstream Example: [10.1 - Simple](https://github.com/handsonscala/handsonscala/tree/v2/examples/10.1%20-%20Simple):
Diff:
```diff
diff --git a/10.1 - Simple/build.mill b/10.2 - Nonlinear/build.mill
index 3364c41..0082352 100644
--- a/10.1 - Simple/build.mill	
+++ b/10.2 - Nonlinear/build.mill	
@@ -1,7 +1,25 @@
 import mill.*
 
 def srcs = Task.Source("src")
+def resources = Task.Source("resources")
 
 def concat = Task:
   os.write(Task.dest / "concat.txt",  os.list(srcs().path).map(os.read(_)))
   PathRef(Task.dest / "concat.txt")
+
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
+  for p <- os.list(compress().path) do
+    os.copy(p, temp / p.relativeTo(compress().path))
+  os.call(cmd = ("zip", "-r", Task.dest / "out.zip", "."), cwd = temp)
+  PathRef(Task.dest / "out.zip")
diff --git a/10.2 - Nonlinear/resources/Design.fw.png b/10.2 - Nonlinear/resources/Design.fw.png
new file mode 100644
index 0000000..3d781b6
Binary files /dev/null and b/10.2 - Nonlinear/resources/Design.fw.png differ
diff --git a/10.2 - Nonlinear/resources/MIT-Seal.gif b/10.2 - Nonlinear/resources/MIT-Seal.gif
new file mode 100644
index 0000000..16e75ef
Binary files /dev/null and b/10.2 - Nonlinear/resources/MIT-Seal.gif differ
```
## Downstream Examples

- [10.3 - Modules](https://github.com/handsonscala/handsonscala/tree/v2/examples/10.3%20-%20Modules)