# Example 9.4 - Links
Adding links between our `index.html` and the individual blog posts

```bash
./mill -i TestBlog.scala
```

## Upstream Example: [9.3 - Markdown](https://github.com/handsonscala/handsonscala/tree/v2/examples/9.3%20-%20Markdown):
Diff:
```diff
diff --git a/9.3 - Markdown/Blog.scala b/9.4 - Links/Blog.scala
index 80ed124..c927498 100644
--- a/9.3 - Markdown/Blog.scala	
+++ b/9.4 - Links/Blog.scala	
@@ -27,7 +27,7 @@ def main() =
       doctype("html")(
         html(
           body(
-            h1(a("Blog"), " / ", suffix),
+            h1(a(href := "../index.html")("Blog"), " / ", suffix),
             raw(output)
           )
         )
@@ -41,7 +41,7 @@ def main() =
         body(
           h1("Blog"),
           for (_, suffix, _) <- postInfo
-          yield h2(suffix)
+          yield h2(a(href := ("post/" + mdNameToHtml(suffix)), suffix))
         )
       )
     )
diff --git a/9.3 - Markdown/TestBlog.scala b/9.4 - Links/TestBlog.scala
index 9cf497c..ced6567 100644
--- a/9.3 - Markdown/TestBlog.scala	
+++ b/9.4 - Links/TestBlog.scala	
@@ -3,6 +3,15 @@
 @main def testMain() =
   main()
 
-  assert(pprint.log(os.read(os.pwd / "out/post/my-first-post.html")).contains(" / My First Post</h1>"))
-  assert(pprint.log(os.read(os.pwd / "out/post/my-second-post.html")).contains(" / My Second Post</h1>"))
-  assert(pprint.log(os.read(os.pwd / "out/post/my-third-post.html")).contains(" / My Third Post</h1>"))
+  assert(
+    pprint.log(os.read(os.pwd / "out/index.html"))
+      .contains("""<h2><a href="post/my-first-post.html">My First Post</a></h2>""")
+  )
+  assert(
+    pprint.log(os.read(os.pwd / "out/index.html"))
+      .contains("""<h2><a href="post/my-second-post.html">My Second Post</a></h2>""")
+  )
+  assert(
+    pprint.log(os.read(os.pwd / "out/index.html"))
+      .contains("""<h2><a href="post/my-third-post.html">My Third Post</a></h2>""")
+  )
```
## Downstream Examples

- [9.5 - Bootstrap](https://github.com/handsonscala/handsonscala/tree/v2/examples/9.5%20-%20Bootstrap)