+def links = Task.Input{ postInfo.map(_(1)) }
+
+def index = Task:
   os.write(
-    os.pwd / "out/index.html",
+    Task.dest / "index.html",
     doctype("html")(
       html(
         head(bootstrapCss),
         body(
           h1("Blog"),
-          for (_, suffix, _) <- postInfo do
+          for suffix <- links() do
           yield h2(a(href := ("post/" + mdNameToHtml(suffix)))(suffix))
         )
       )
     )
   )
+  PathRef(Task.dest / "index.html")