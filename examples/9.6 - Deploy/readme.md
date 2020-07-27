# Example 9.6 - Deploy
Optionally deploying our static blog to a Git repository

```bash
amm Blog.sc
amm TestBlog.sc
amm Blog.sc git@github.com:lihaoyi/test.git
amm Blog.sc --targetGitRepo git@github.com:lihaoyi/test.git
```

## Upstream Example: [9.5 - Bootstrap](https://github.com/handsonscala/handsonscala/tree/v1/examples/9.5%20-%20Bootstrap):
Diff:
```diff
diff --git a/9.5 - Bootstrap/Blog.sc b/9.6 - Deploy/Blog.sc
index f0412cb..3bb32bf 100644
--- a/9.5 - Bootstrap/Blog.sc	
+++ b/9.6 - Deploy/Blog.sc	
@@ -1,8 +1,9 @@
 import $ivy.`com.lihaoyi::scalatags:0.9.1`, scalatags.Text.all._
 import $ivy.`com.atlassian.commonmark:commonmark:0.13.1`
 
-interp.watch(os.pwd / "post")
-val postInfo = os
+@main def main(targetGitRepo: String = ""): Unit = {
+  interp.watch(os.pwd / "post")
+  val postInfo = os
     .list(os.pwd / "post")
     .map{ p =>
       val s"$prefix - $suffix.md" = p.last
@@ -10,17 +11,17 @@ val postInfo = os
     }
     .sortBy(_._1.toInt)
 
-def mdNameToHtml(name: String) = name.replace(" ", "-").toLowerCase + ".html"
+  def mdNameToHtml(name: String) = name.replace(" ", "-").toLowerCase + ".html"
 
-val bootstrapCss = link(
+  val bootstrapCss = link(
     rel := "stylesheet",
     href := "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
-)
+  )
 
-os.remove.all(os.pwd / "out")
-os.makeDir.all(os.pwd / "out" / "post")
+  os.remove.all(os.pwd / "out")
+  os.makeDir.all(os.pwd / "out" / "post")
 
-for ((_, suffix, path) <- postInfo) {
+  for ((_, suffix, path) <- postInfo) {
     val parser = org.commonmark.parser.Parser.builder().build()
     val document = parser.parse(os.read(path))
     val renderer = org.commonmark.renderer.html.HtmlRenderer.builder().build()
@@ -31,15 +32,15 @@ for ((_, suffix, path) <- postInfo) {
         html(
           head(bootstrapCss),
           body(
-          h1(a(href := "../index.html")("Blog"), " / ", suffix),
+            h1(a("Blog", href := "../index.html"), " / ", suffix),
             raw(output)
           )
         )
       )
     )
-}
+  }
 
-os.write(
+  os.write(
     os.pwd / "out" / "index.html",
     doctype("html")(
       html(
@@ -51,4 +52,12 @@ os.write(
         )
       )
     )
-)
+  )
+
+  if (targetGitRepo != "") {
+    os.proc("git", "init").call(cwd = os.pwd / "out")
+    os.proc("git", "add", "-A").call(cwd = os.pwd / "out")
+    os.proc("git", "commit", "-am", ".").call(cwd = os.pwd / "out")
+    os.proc("git", "push", targetGitRepo, "head", "-f").call(cwd = os.pwd / "out")
+  }
+}
```
## Downstream Examples

- [9.7 - DeployTimestamp](https://github.com/handsonscala/handsonscala/tree/v1/examples/9.7%20-%20DeployTimestamp)
- [10.6 - Blog](https://github.com/handsonscala/handsonscala/tree/v1/examples/10.6%20-%20Blog)