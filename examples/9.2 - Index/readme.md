# Example 9.2 - Index
Rendering an `index.html` for our static blog using Scalatags

```bash
amm TestBlog.sc
```

## Upstream Example: [9.1 - Printing](https://github.com/handsonscala/handsonscala/tree/v1/examples/9.1%20-%20Printing):
Diff:
```diff
diff --git a/9.1 - Printing/Blog.sc b/9.2 - Index/Blog.sc
index aee5e08..573a7a3 100644
--- a/9.1 - Printing/Blog.sc	
+++ b/9.2 - Index/Blog.sc	
@@ -1,3 +1,5 @@
+import $ivy.`com.lihaoyi::scalatags:0.9.1`, scalatags.Text.all._
+
 interp.watch(os.pwd / "post")
 val postInfo = os
   .list(os.pwd / "post")
@@ -7,7 +9,18 @@ val postInfo = os
   }
   .sortBy(_._1.toInt)
 
-assert(
-  pprint.log(postInfo.map(t => (t._1, t._2))) ==
-  Seq("1" -> "My First Post", "2" -> "My Second Post", "3" -> "My Third Post")
+os.remove.all(os.pwd / "out")
+os.makeDir.all(os.pwd / "out" / "post")
+
+os.write(
+  os.pwd / "out" / "index.html",
+  doctype("html")(
+    html(
+      body(
+        h1("Blog"),
+        for ((_, suffix, _) <- postInfo)
+        yield h2(suffix)
+      )
+    )
+  )
 )
diff --git a/9.2 - Index/TestBlog.sc b/9.2 - Index/TestBlog.sc
new file mode 100644
index 0000000..6c3b120
--- /dev/null
+++ b/9.2 - Index/TestBlog.sc	
@@ -0,0 +1,6 @@
+import $file.Blog, Blog._
+
+val indexHtml = pprint.log(os.read(os.pwd / "out" / "index.html"))
+assert(indexHtml.contains("<h2>My First Post</h2>"))
+assert(indexHtml.contains("<h2>My Second Post</h2>"))
+assert(indexHtml.contains("<h2>My Third Post</h2>"))
```
## Downstream Examples

- [9.3 - Markdown](https://github.com/handsonscala/handsonscala/tree/v1/examples/9.3%20-%20Markdown)