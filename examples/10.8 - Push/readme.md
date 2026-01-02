# Example 10.8 - Push
Re-adding the ability to deploy our static blog by defining a `Task.Command`

```bash
./mill -i push --targetGitRepo git@github.com:lihaoyi/test.git
```

## Upstream Example: [10.7 - ExtendedBlog](https://github.com/handsonscala/handsonscala/tree/v2/examples/10.7%20-%20ExtendedBlog):
Diff:
```diff
diff --git a/10.7 - ExtendedBlog/build.mill b/10.8 - Push/build.mill
index 055c61a..3888f62 100644
--- a/10.7 - ExtendedBlog/build.mill	
+++ b/10.8 - Push/build.mill	
@@ -74,7 +74,15 @@ def index = Task:
 def dist = Task:
   for post <- posts() do
     os.copy(post.path, Task.dest / "post" / post.path.last, createFolders = true)
-
   os.copy(index().path, Task.dest / "index.html")
   os.copy(bootstrap().path, Task.dest / "bootstrap.css")
   PathRef(Task.dest)
+
+def push(targetGitRepo: String = "") = Task.Command:
+  for p <- os.list(dist().path) do os.copy(p, Task.dest / p.last)
+
+  os.call(cmd = ("git", "init"), cwd = Task.dest)
+  os.call(cmd = ("git", "add", "-A"), cwd = Task.dest)
+  os.call(cmd = ("git", "commit", "-am", "."), cwd = Task.dest)
+  os.call(cmd = ("git", "push", targetGitRepo, "HEAD", "-f"), cwd = Task.dest)
+
```
