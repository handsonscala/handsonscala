# Example 9.7 - DeployTimestamp
Displaying the `.md` file last-modified time on each blog post

```bash
./mill -i Blog.scala
./mill -i TestBlog.scala
./mill -i Blog.scala --target-git-repo git@github.com:lihaoyi/test.git
```

## Upstream Example: [9.6 - Deploy](https://github.com/handsonscala/handsonscala/tree/v2/examples/9.6%20-%20Deploy):
Diff:
```diff
diff --git a/9.6 - Deploy/Blog.scala b/9.7 - DeployTimestamp/Blog.scala
index 4a76700..6a393f9 100644
--- a/9.6 - Deploy/Blog.scala	
+++ b/9.7 - DeployTimestamp/Blog.scala	
@@ -8,7 +8,11 @@ def main(targetGitRepo: String = "") =
     .list(os.pwd / "post")
     .map: p =>
       val s"$prefix - $suffix.md" = p.last
-      (prefix, suffix, p)
+      val publishDate = java.time.LocalDate.ofInstant(
+        java.time.Instant.ofEpochMilli(os.mtime(p)),
+        java.time.ZoneOffset.UTC
+      )
+      (prefix, suffix, p, publishDate)
     .sortBy(_(0).toInt)
 
   def mdNameToHtml(name: String) =
@@ -22,23 +26,26 @@ def main(targetGitRepo: String = "") =
   os.remove.all(os.pwd / "out")
   os.makeDir.all(os.pwd / "out/post")
 
-  for (_, suffix, path) <- postInfo do
+  for (_, suffix, path, publishDate) <- postInfo do
     val parser = org.commonmark.parser.Parser.builder().build()
     val document = parser.parse(os.read(path))
     val renderer = org.commonmark.renderer.html.HtmlRenderer.builder().build()
     val output = renderer.render(document)
+
     os.write(
       os.pwd / "out/post" / mdNameToHtml(suffix),
       doctype("html")(
         html(
           head(bootstrapCss),
           body(
-            h1(a(href := "../index.html")("Blog"), " / ", suffix),
-            raw(output)
+            h1(a("Blog", href := "../index.html"), " / ", suffix),
+            raw(output),
+            p(i("Written on " + publishDate))
           )
         )
       )
     )
+  end for
 
   os.write(
     os.pwd / "out/index.html",
@@ -47,8 +54,11 @@ def main(targetGitRepo: String = "") =
         head(bootstrapCss),
         body(
           h1("Blog"),
-          for (_, suffix, _) <- postInfo
-          yield h2(a(href := ("post/" + mdNameToHtml(suffix)), suffix))
+          for (_, suffix, _, publishDate) <- postInfo
+          yield frag(
+            h2(a(href := ("post/" + mdNameToHtml(suffix)))(suffix)),
+            p(i("Written on " + publishDate))
+          )
         )
       )
     )
diff --git a/9.6 - Deploy/TestBlog.scala b/9.7 - DeployTimestamp/TestBlog.scala
index c7ed5de..af8f7d9 100644
--- a/9.6 - Deploy/TestBlog.scala	
+++ b/9.7 - DeployTimestamp/TestBlog.scala	
@@ -3,19 +3,7 @@
 @main def testMain() =
   main()
 
-  assert(
-    pprint.log(os.read(os.pwd / "out/index.html"))
-      .contains("""<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css" />""")
-  )
-  assert(
-    pprint.log(os.read(os.pwd / "out/post/my-first-post.html"))
-      .contains("""<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css" />""")
-  )
-  assert(
-    pprint.log(os.read(os.pwd / "out/post/my-second-post.html"))
-      .contains("""<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css" />""")
-  )
-  assert(
-    pprint.log(os.read(os.pwd / "out/post/my-third-post.html"))
-      .contains("""<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css" />""")
-  )
+  assert(pprint.log(os.read(os.pwd / "out/index.html")).contains("Written on 2025-"))
+  assert(pprint.log(os.read(os.pwd / "out/post/my-first-post.html")).contains("Written on 2025-"))
+  assert(pprint.log(os.read(os.pwd / "out/post/my-second-post.html")).contains("Written on 2025-"))
+  assert(pprint.log(os.read(os.pwd / "out/post/my-third-post.html")).contains("Written on 2025-"))
```
