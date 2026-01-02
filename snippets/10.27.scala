 def dist = Task:
   for post <- posts() do
     os.copy(post.path, Task.dest / "post" / post.path.last, createFolders = true)

   os.copy(index().path, Task.dest / "index.html")
+  os.copy(bootstrap().path, Task.dest / "bootstrap.css")
   PathRef(Task.dest)