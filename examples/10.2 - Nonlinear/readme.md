# Example 10.2 - Nonlinear
Simple non-linear build pipeline with two branches

```bash
./mill -i zipped
```

## Upstream Example: [10.1 - Simple](https://github.com/handsonscala/handsonscala/tree/v1/examples/10.1%20-%20Simple):
Diff:
```diff
diff --git a/10.1 - Simple/build.sc b/10.2 - Nonlinear/build.sc
index 9c67388..ce4c01f 100644
--- a/10.1 - Simple/build.sc	
+++ b/10.2 - Nonlinear/build.sc	
@@ -1,8 +1,26 @@
 import mill._
 
 def srcs = T.source(millSourcePath / "src")
+def resources = T.source(millSourcePath / "resources")
 
 def concat = T{
   os.write(T.dest / "concat.txt", os.list(srcs().path).map(os.read(_)))
   PathRef(T.dest / "concat.txt")
 }
+
+def compress = T{
+  for (p <- os.list(resources().path)) {
+    val copied = T.dest / p.relativeTo(resources().path)
+    os.copy(p, copied)
+    os.proc("gzip", copied).call()
+  }
+  PathRef(T.dest)
+}
+def zipped = T{
+  val temp = T.dest / "temp"
+  os.makeDir(temp)
+  os.copy(concat().path, temp / "concat.txt")
+  for (p <- os.list(compress().path)) os.copy(p, temp / p.relativeTo(compress().path))
+  os.proc("zip", "-r", T.dest / "out.zip", ".").call(cwd = temp)
+  PathRef(T.dest / "out.zip")
+}
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

- [10.3 - Modules](https://github.com/handsonscala/handsonscala/tree/v1/examples/10.3%20-%20Modules)