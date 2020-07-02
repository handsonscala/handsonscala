# Example 9.3 - Markdown
Rendering individual blog posts using Atlassian's Commonmark-Java library

```bash
amm TestBlog.sc
```

## Upstream Example: [9.2 - Index](https://github.com/handsonscala/handsonscala/tree/v1/examples/9.2%20-%20Index):
Diff:
```diff
diff --git a/9.2 - Index/Blog.sc b/9.3 - Markdown/Blog.sc
index 573a7a3..e046e17 100644
--- a/9.2 - Index/Blog.sc	
+++ b/9.3 - Markdown/Blog.sc	
@@ -1,4 +1,5 @@
 import $ivy.`com.lihaoyi::scalatags:0.9.1`, scalatags.Text.all._
+import $ivy.`com.atlassian.commonmark:commonmark:0.13.1`
 
 interp.watch(os.pwd / "post")
 val postInfo = os
@@ -9,9 +10,29 @@ val postInfo = os
   }
   .sortBy(_._1.toInt)
 
+def mdNameToHtml(name: String) = name.replace(" ", "-").toLowerCase + ".html"
+
 os.remove.all(os.pwd / "out")
 os.makeDir.all(os.pwd / "out" / "post")
 
+for ((_, suffix, path) <- postInfo) {
+  val parser = org.commonmark.parser.Parser.builder().build()
+  val document = parser.parse(os.read(path))
+  val renderer = org.commonmark.renderer.html.HtmlRenderer.builder().build()
+  val output = renderer.render(document)
+  os.write(
+    os.pwd / "out" / "post" / mdNameToHtml(suffix),
+    doctype("html")(
+      html(
+        body(
+          h1(a("Blog"), " / ", suffix),
+          raw(output)
+        )
+      )
+    )
+  )
+}
+
 os.write(
   os.pwd / "out" / "index.html",
   doctype("html")(
diff --git a/9.2 - Index/TestBlog.sc b/9.3 - Markdown/TestBlog.sc
index 6c3b120..fa7ee6b 100644
--- a/9.2 - Index/TestBlog.sc	
+++ b/9.3 - Markdown/TestBlog.sc	
@@ -1,6 +1,5 @@
 import $file.Blog, Blog._
 
-val indexHtml = pprint.log(os.read(os.pwd / "out" / "index.html"))
-assert(indexHtml.contains("<h2>My First Post</h2>"))
-assert(indexHtml.contains("<h2>My Second Post</h2>"))
-assert(indexHtml.contains("<h2>My Third Post</h2>"))
+assert(pprint.log(os.read(os.pwd / "out" / "post" / "my-first-post.html")).contains(" / My First Post</h1>"))
+assert(pprint.log(os.read(os.pwd / "out" / "post" / "my-second-post.html")).contains(" / My Second Post</h1>"))
+assert(pprint.log(os.read(os.pwd / "out" / "post" / "my-third-post.html")).contains(" / My Third Post</h1>"))
```
## Downstream Examples

- [9.4 - Links](https://github.com/handsonscala/handsonscala/tree/v1/examples/9.4%20-%20Links)