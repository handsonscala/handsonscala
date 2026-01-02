# Example 9.3 - Markdown
Rendering individual blog posts using Atlassian's Commonmark-Java library

```bash
./mill -i TestBlog.scala
```

## Upstream Example: [9.2 - Index](https://github.com/handsonscala/handsonscala/tree/v2/examples/9.2%20-%20Index):
Diff:
```diff
diff --git a/9.2 - Index/Blog.scala b/9.3 - Markdown/Blog.scala
index 113e1f7..80ed124 100644
--- a/9.2 - Index/Blog.scala	
+++ b/9.3 - Markdown/Blog.scala	
@@ -1,7 +1,11 @@
 //| mvnDeps:
 //| - com.lihaoyi::scalatags:0.13.1
+//| - org.commonmark:commonmark:0.26.0
 import scalatags.Text.all.*
 
+def mdNameToHtml(name: String) =
+  name.replace(" ", "-").toLowerCase + ".html"
+
 def main() =
   val postInfo = os
     .list(os.pwd / "post")
@@ -13,6 +17,23 @@ def main() =
   os.remove.all(os.pwd / "out")
   os.makeDir.all(os.pwd / "out/post")
 
+  for (_, suffix, path) <- postInfo do
+    val parser = org.commonmark.parser.Parser.builder().build()
+    val document = parser.parse(os.read(path))
+    val renderer = org.commonmark.renderer.html.HtmlRenderer.builder().build()
+    val output = renderer.render(document)
+    os.write(
+      os.pwd / "out/post" / mdNameToHtml(suffix),
+      doctype("html")(
+        html(
+          body(
+            h1(a("Blog"), " / ", suffix),
+            raw(output)
+          )
+        )
+      )
+    )
+
   os.write(
     os.pwd / "out/index.html",
     doctype("html")(
diff --git a/9.2 - Index/TestBlog.scala b/9.3 - Markdown/TestBlog.scala
index d1d8c70..9cf497c 100644
--- a/9.2 - Index/TestBlog.scala	
+++ b/9.3 - Markdown/TestBlog.scala	
@@ -3,7 +3,6 @@
 @main def testMain() =
   main()
 
-  val indexHtml = pprint.log(os.read(os.pwd / "out/index.html"))
-  assert(indexHtml.contains("<h2>My First Post</h2>"))
-  assert(indexHtml.contains("<h2>My Second Post</h2>"))
-  assert(indexHtml.contains("<h2>My Third Post</h2>"))
+  assert(pprint.log(os.read(os.pwd / "out/post/my-first-post.html")).contains(" / My First Post</h1>"))
+  assert(pprint.log(os.read(os.pwd / "out/post/my-second-post.html")).contains(" / My Second Post</h1>"))
+  assert(pprint.log(os.read(os.pwd / "out/post/my-third-post.html")).contains(" / My Third Post</h1>"))
```
## Downstream Examples

- [9.4 - Links](https://github.com/handsonscala/handsonscala/tree/v2/examples/9.4%20-%20Links)