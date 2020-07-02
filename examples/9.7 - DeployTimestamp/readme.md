# Example 9.7 - DeployTimestamp
Displaying the `.md` file last-modified time on each blog post

```bash
amm Blog.sc
amm TestBlog.sc
amm Blog.sc git@github.com:lihaoyi/test.git
amm Blog.sc --targetGitRepo git@github.com:lihaoyi/test.git
```

## Upstream Example: [9.6 - Deploy](https://github.com/handsonscala/handsonscala/tree/v1/examples/9.6%20-%20Deploy):
Diff:
```diff
diff --git a/9.6 - Deploy/Blog.sc b/9.7 - DeployTimestamp/Blog.sc
index 3bb32bf..a2285e7 100644
--- a/9.6 - Deploy/Blog.sc	
+++ b/9.7 - DeployTimestamp/Blog.sc	
@@ -7,7 +7,11 @@ import $ivy.`com.atlassian.commonmark:commonmark:0.13.1`
     .list(os.pwd / "post")
     .map{ p =>
       val s"$prefix - $suffix.md" = p.last
-      (prefix, suffix, p)
+      val publishDate = java.time.LocalDate.ofInstant(
+        java.time.Instant.ofEpochMilli(os.mtime(p)),
+        java.time.ZoneOffset.UTC
+      )
+      (prefix, suffix, p, publishDate)
     }
     .sortBy(_._1.toInt)
 
@@ -21,11 +25,12 @@ import $ivy.`com.atlassian.commonmark:commonmark:0.13.1`
   os.remove.all(os.pwd / "out")
   os.makeDir.all(os.pwd / "out" / "post")
 
-  for ((_, suffix, path) <- postInfo) {
+  for ((_, suffix, path, publishDate) <- postInfo) {
     val parser = org.commonmark.parser.Parser.builder().build()
     val document = parser.parse(os.read(path))
     val renderer = org.commonmark.renderer.html.HtmlRenderer.builder().build()
     val output = renderer.render(document)
+
     os.write(
       os.pwd / "out" / "post" / mdNameToHtml(suffix),
       doctype("html")(
@@ -33,7 +38,8 @@ import $ivy.`com.atlassian.commonmark:commonmark:0.13.1`
           head(bootstrapCss),
           body(
             h1(a("Blog", href := "../index.html"), " / ", suffix),
-            raw(output)
+            raw(output),
+            p(i("Written on " + publishDate))
           )
         )
       )
@@ -47,8 +53,11 @@ import $ivy.`com.atlassian.commonmark:commonmark:0.13.1`
         head(bootstrapCss),
         body(
           h1("Blog"),
-          for ((_, suffix, _) <- postInfo)
-          yield h2(a(href := ("post/" + mdNameToHtml(suffix)))(suffix))
+          for ((_, suffix, _, publishDate) <- postInfo)
+          yield frag(
+            h2(a(href := ("post/" + mdNameToHtml(suffix)))(suffix)),
+            p(i("Written on " + publishDate))
+          )
         )
       )
     )
diff --git a/9.6 - Deploy/TestBlog.sc b/9.7 - DeployTimestamp/TestBlog.sc
index 4ab6f4b..38a922f 100644
--- a/9.6 - Deploy/TestBlog.sc	
+++ b/9.7 - DeployTimestamp/TestBlog.sc	
@@ -1,18 +1,6 @@
 import $file.Blog, Blog._
 
-assert(
-  pprint.log(os.read(os.pwd / "out" / "index.html"))
-    .contains("""<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css" />""")
-)
-assert(
-  pprint.log(os.read(os.pwd / "out" / "post" / "my-first-post.html"))
-    .contains("""<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css" />""")
-)
-assert(
-  pprint.log(os.read(os.pwd / "out" / "post" / "my-second-post.html"))
-    .contains("""<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css" />""")
-)
-assert(
-  pprint.log(os.read(os.pwd / "out" / "post" / "my-third-post.html"))
-    .contains("""<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css" />""")
-)
+assert(pprint.log(os.read(os.pwd / "out" / "index.html")).contains("Written on 2020-"))
+assert(pprint.log(os.read(os.pwd / "out" / "post" / "my-first-post.html")).contains("Written on 2020-"))
+assert(pprint.log(os.read(os.pwd / "out" / "post" / "my-second-post.html")).contains("Written on 2020-"))
+assert(pprint.log(os.read(os.pwd / "out" / "post" / "my-third-post.html")).contains("Written on 2020-"))
```
