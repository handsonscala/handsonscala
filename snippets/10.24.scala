+val posts = Task.sequence(postInfo.map(_(0)).map(post(_).render))
+
+def dist = Task:
+  for post <- posts() do
+    os.copy(post.path, Task.dest / "post" / post.path.last, createFolders = true)
+
+  os.copy(index().path, Task.dest / "index.html")
+
+  PathRef(Task.dest)