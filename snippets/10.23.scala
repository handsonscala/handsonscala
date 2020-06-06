+val posts = T.sequence(postInfo.map(_._1).map(post(_).render))
+
+def dist = T {
+  for (post <- posts()) {
+    os.copy(post.path, T.dest / "post" / post.path.last, createFolders = true)
+  }
+  os.copy(index().path, T.dest / "index.html")
+
+  PathRef(T.dest)
+}