# Example 9.5 - Bootstrap
Prettifying our static blog using the Bootstrap CSS framework

```bash
./mill -i TestBlog.scala
```

## Upstream Example: [9.4 - Links](https://github.com/handsonscala/handsonscala/tree/v2/examples/9.4%20-%20Links):
Diff:
```diff
diff --git a/9.4 - Links/Blog.scala b/9.5 - Bootstrap/Blog.scala
index c927498..fc4d7ea 100644
--- a/9.4 - Links/Blog.scala	
+++ b/9.5 - Bootstrap/Blog.scala	
@@ -6,6 +6,11 @@ import scalatags.Text.all.*
 def mdNameToHtml(name: String) =
   name.replace(" ", "-").toLowerCase + ".html"
 
+val bootstrapCss = link(
+  rel := "stylesheet",
+  href := "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
+)
+
 def main() =
   val postInfo = os
     .list(os.pwd / "post")
@@ -26,6 +31,7 @@ def main() =
       os.pwd / "out/post" / mdNameToHtml(suffix),
       doctype("html")(
         html(
+          head(bootstrapCss),
           body(
             h1(a(href := "../index.html")("Blog"), " / ", suffix),
             raw(output)
@@ -38,6 +44,7 @@ def main() =
     os.pwd / "out/index.html",
     doctype("html")(
       html(
+        head(bootstrapCss),
         body(
           h1("Blog"),
           for (_, suffix, _) <- postInfo
diff --git a/9.4 - Links/TestBlog.scala b/9.5 - Bootstrap/TestBlog.scala
index ced6567..c7ed5de 100644
--- a/9.4 - Links/TestBlog.scala	
+++ b/9.5 - Bootstrap/TestBlog.scala	
@@ -5,13 +5,17 @@
 
   assert(
     pprint.log(os.read(os.pwd / "out/index.html"))
-      .contains("""<h2><a href="post/my-first-post.html">My First Post</a></h2>""")
+      .contains("""<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css" />""")
   )
   assert(
-    pprint.log(os.read(os.pwd / "out/index.html"))
-      .contains("""<h2><a href="post/my-second-post.html">My Second Post</a></h2>""")
+    pprint.log(os.read(os.pwd / "out/post/my-first-post.html"))
+      .contains("""<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css" />""")
   )
   assert(
-    pprint.log(os.read(os.pwd / "out/index.html"))
-      .contains("""<h2><a href="post/my-third-post.html">My Third Post</a></h2>""")
+    pprint.log(os.read(os.pwd / "out/post/my-second-post.html"))
+      .contains("""<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css" />""")
+  )
+  assert(
+    pprint.log(os.read(os.pwd / "out/post/my-third-post.html"))
+      .contains("""<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css" />""")
   )
```
## Downstream Examples

- [9.6 - Deploy](https://github.com/handsonscala/handsonscala/tree/v2/examples/9.6%20-%20Deploy)