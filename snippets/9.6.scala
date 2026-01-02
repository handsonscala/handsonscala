+//| mvnDeps:
+//| - com.lihaoyi::scalatags:0.13.1
+import scalatags.Text.all.*
 val postInfo = ... 
-println("POSTS")
-postInfo.foreach(println)
+os.remove.all(os.pwd / "out")
+os.makeDir.all(os.pwd / "out/post")
+os.write(
+  os.pwd / "out/index.html",
+  doctype("html")(
+    html(
+      body(
+        h1("Blog"),
+        for (_, suffix, _) <- postInfo
+        yield h2(suffix)
+      )
+    )
+  )
+)