# Example 10.8 - Push
Re-adding the ability to deploy our static blog by defining a `T.command`

```bash
./mill -i push git@github.com:lihaoyi/test.git
```

## Upstream Example: [10.7 - ExtendedBlog](https://github.com/handsonscala/handsonscala/tree/v1/examples/10.7%20-%20ExtendedBlog):
Diff:
```diff
diff --git a/10.7 - ExtendedBlog/build.sc b/10.8 - Push/build.sc
index f6174af..995ea5a 100644
--- a/10.7 - ExtendedBlog/build.sc	
+++ b/10.8 - Push/build.sc	
@@ -80,3 +80,12 @@ def dist = T {
   os.copy(bootstrap().path, T.dest / "bootstrap.css")
   PathRef(T.dest)
 }
+
+def push(targetGitRepo: String = "") = T.command{
+  for(p <- os.list(dist().path)) os.copy(p, T.dest / p.last)
+
+  os.proc("git", "init").call(cwd = T.dest)
+  os.proc("git", "add", "-A").call(cwd = T.dest)
+  os.proc("git", "commit", "-am", ".").call(cwd = T.dest)
+  os.proc("git", "push", targetGitRepo, "head", "-f").call(cwd = T.dest)
+}
```
