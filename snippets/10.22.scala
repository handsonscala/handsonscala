+def links = T.input{ postInfo.map(_._2) }
+
+def index = T{
   os.write(
-    os.pwd / "out" / "index.html",
+    T.dest / "index.html",
     doctype("html")(
       html(
         head(bootstrapCss),
         body(
           h1("Blog"),
-          for ((_, suffix, _) <- postInfo)
+          for (suffix <- links())
           yield h2(a(href := ("post/" + mdNameToHtml(suffix)))(suffix))
         )
       )
     )
   )
+  PathRef(T.dest / "index.html")
+}