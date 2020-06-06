# Example 9.5 - Bootstrap
Prettifying our static blog using the Bootstrap CSS framework

```bash
amm TestBlog.sc
```

## Upstream Example: [9.4 - Links](https://github.com/handsonscala/handsonscala/tree/v1/examples/9.4%20-%20Links):
Diff:
```diff
diff --git a/9.4 - Links/Blog.sc b/9.5 - Bootstrap/Blog.sc
index 3df1d9c..f0412cb 100644
--- a/9.4 - Links/Blog.sc	
+++ b/9.5 - Bootstrap/Blog.sc	
@@ -12,6 +12,11 @@ val postInfo = os
 
 def mdNameToHtml(name: String) = name.replace(" ", "-").toLowerCase + ".html"
 
+val bootstrapCss = link(
+  rel := "stylesheet",
+  href := "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
+)
+
 os.remove.all(os.pwd / "out")
 os.makeDir.all(os.pwd / "out" / "post")
 
@@ -24,6 +29,7 @@ for ((_, suffix, path) <- postInfo) {
     os.pwd / "out" / "post" / mdNameToHtml(suffix),
     doctype("html")(
       html(
+        head(bootstrapCss),
         body(
           h1(a(href := "../index.html")("Blog"), " / ", suffix),
           raw(output)
@@ -37,6 +43,7 @@ os.write(
   os.pwd / "out" / "index.html",
   doctype("html")(
     html(
+      head(bootstrapCss),
       body(
         h1("Blog"),
         for ((_, suffix, _) <- postInfo)
diff --git a/9.4 - Links/TestBlog.sc b/9.5 - Bootstrap/TestBlog.sc
index c388e58..4ab6f4b 100644
--- a/9.4 - Links/TestBlog.sc	
+++ b/9.5 - Bootstrap/TestBlog.sc	
@@ -2,13 +2,17 @@ import $file.Blog, Blog._
 
 assert(
   pprint.log(os.read(os.pwd / "out" / "index.html"))
-    .contains("""<h2><a href="post/my-first-post.html">My First Post</a></h2>""")
+    .contains("""<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css" />""")
 )
 assert(
-  pprint.log(os.read(os.pwd / "out" / "index.html"))
-    .contains("""<h2><a href="post/my-second-post.html">My Second Post</a></h2>""")
+  pprint.log(os.read(os.pwd / "out" / "post" / "my-first-post.html"))
+    .contains("""<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css" />""")
 )
 assert(
-  pprint.log(os.read(os.pwd / "out" / "index.html"))
-    .contains("""<h2><a href="post/my-third-post.html">My Third Post</a></h2>""")
+  pprint.log(os.read(os.pwd / "out" / "post" / "my-second-post.html"))
+    .contains("""<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css" />""")
+)
+assert(
+  pprint.log(os.read(os.pwd / "out" / "post" / "my-third-post.html"))
+    .contains("""<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css" />""")
 )
```
## Downstream Examples

- [9.6 - Deploy](https://github.com/handsonscala/handsonscala/tree/v1/examples/9.6%20-%20Deploy)