 def dist = T {
   for (post <- posts()) {
     os.copy(post.path, T.dest / "post" / post.path.last, createFolders = true)
   }
   os.copy(index().path, T.dest / "index.html")
+  os.copy(bootstrap().path, T.dest / "bootstrap.css")
   PathRef(T.dest)
 }