# Example 9.4 - Links
Adding links between our `index.html` and the individual blog posts

```bash
amm TestBlog.sc
```

## Upstream Example: [9.3 - Markdown](https://github.com/handsonscala/handsonscala/tree/v1/examples/9.3%20-%20Markdown):
Diff:
```diff
diff --git a/9.3 - Markdown/Blog.sc b/9.4 - Links/Blog.sc
index e046e17..3df1d9c 100644
--- a/9.3 - Markdown/Blog.sc	
+++ b/9.4 - Links/Blog.sc	
@@ -25,7 +25,7 @@ for ((_, suffix, path) <- postInfo) {
     doctype("html")(
       html(
         body(
-          h1(a("Blog"), " / ", suffix),
+          h1(a(href := "../index.html")("Blog"), " / ", suffix),
           raw(output)
         )
       )
@@ -40,7 +40,7 @@ os.write(
       body(
         h1("Blog"),
         for ((_, suffix, _) <- postInfo)
-        yield h2(suffix)
+        yield h2(a(href := ("post/" + mdNameToHtml(suffix)))(suffix))
       )
     )
   )
diff --git a/9.3 - Markdown/TestBlog.sc b/9.4 - Links/TestBlog.sc
index fa7ee6b..c388e58 100644
--- a/9.3 - Markdown/TestBlog.sc	
+++ b/9.4 - Links/TestBlog.sc	
@@ -1,5 +1,14 @@
 import $file.Blog, Blog._
 
-assert(pprint.log(os.read(os.pwd / "out" / "post" / "my-first-post.html")).contains(" / My First Post</h1>"))
-assert(pprint.log(os.read(os.pwd / "out" / "post" / "my-second-post.html")).contains(" / My Second Post</h1>"))
-assert(pprint.log(os.read(os.pwd / "out" / "post" / "my-third-post.html")).contains(" / My Third Post</h1>"))
+assert(
+  pprint.log(os.read(os.pwd / "out" / "index.html"))
+    .contains("""<h2><a href="post/my-first-post.html">My First Post</a></h2>""")
+)
+assert(
+  pprint.log(os.read(os.pwd / "out" / "index.html"))
+    .contains("""<h2><a href="post/my-second-post.html">My Second Post</a></h2>""")
+)
+assert(
+  pprint.log(os.read(os.pwd / "out" / "index.html"))
+    .contains("""<h2><a href="post/my-third-post.html">My Third Post</a></h2>""")
+)
```
## Downstream Examples

- [9.5 - Bootstrap](https://github.com/handsonscala/handsonscala/tree/v1/examples/9.5%20-%20Bootstrap)