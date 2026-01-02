# Example 10.6 - Blog
Our static blog generator, converted into an incremental Mill build pipeline

```bash
./mill -i dist
```

## Upstream Example: [9.6 - Deploy](https://github.com/handsonscala/handsonscala/tree/v2/examples/9.6%20-%20Deploy):
Diff:
```diff
diff --git a/9.6 - Deploy/Blog.scala b/9.6 - Deploy/Blog.scala
deleted file mode 100644
index 4a76700..0000000
--- a/9.6 - Deploy/Blog.scala	
+++ /dev/null
@@ -1,61 +0,0 @@
-//| mvnDeps:
-//| - com.lihaoyi::scalatags:0.13.1
-//| - org.commonmark:commonmark:0.26.0
-import scalatags.Text.all.*
-
-def main(targetGitRepo: String = "") =
-  val postInfo = os
-    .list(os.pwd / "post")
-    .map: p =>
-      val s"$prefix - $suffix.md" = p.last
-      (prefix, suffix, p)
-    .sortBy(_(0).toInt)
-
-  def mdNameToHtml(name: String) =
-    name.replace(" ", "-").toLowerCase + ".html"
-
-  val bootstrapCss = link(
-    rel := "stylesheet",
-    href := "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
-  )
-
-  os.remove.all(os.pwd / "out")
-  os.makeDir.all(os.pwd / "out/post")
-
-  for (_, suffix, path) <- postInfo do
-    val parser = org.commonmark.parser.Parser.builder().build()
-    val document = parser.parse(os.read(path))
-    val renderer = org.commonmark.renderer.html.HtmlRenderer.builder().build()
-    val output = renderer.render(document)
-    os.write(
-      os.pwd / "out/post" / mdNameToHtml(suffix),
-      doctype("html")(
-        html(
-          head(bootstrapCss),
-          body(
-            h1(a(href := "../index.html")("Blog"), " / ", suffix),
-            raw(output)
-          )
-        )
-      )
-    )
-
-  os.write(
-    os.pwd / "out/index.html",
-    doctype("html")(
-      html(
-        head(bootstrapCss),
-        body(
-          h1("Blog"),
-          for (_, suffix, _) <- postInfo
-          yield h2(a(href := ("post/" + mdNameToHtml(suffix)), suffix))
-        )
-      )
-    )
-  )
-
-  if targetGitRepo != "" then
-    os.call(cmd = ("git", "init"), cwd = os.pwd / "out")
-    os.call(cmd = ("git", "add", "-A"), cwd = os.pwd / "out")
-    os.call(cmd = ("git", "commit", "-am", "."), cwd = os.pwd / "out")
-    os.call(cmd = ("git", "push", targetGitRepo, "head", "-f"), cwd = os.pwd / "out")
diff --git a/9.6 - Deploy/TestBlog.scala b/9.6 - Deploy/TestBlog.scala
deleted file mode 100644
index c7ed5de..0000000
--- a/9.6 - Deploy/TestBlog.scala	
+++ /dev/null
@@ -1,21 +0,0 @@
-//| moduleDeps: [Blog.scala]
-
-@main def testMain() =
-  main()
-
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
diff --git a/10.6 - Blog/build.mill b/10.6 - Blog/build.mill
new file mode 100644
index 0000000..30a5e36
--- /dev/null
+++ b/10.6 - Blog/build.mill	
@@ -0,0 +1,71 @@
+//| mvnDeps:
+//| - com.lihaoyi::scalatags:0.13.1
+//| - org.commonmark:commonmark:0.26.0
+import mill.*
+import mill.api.BuildCtx
+import scalatags.Text.all.*
+
+def mdNameToHtml(name: String) =
+  name.replace(" ", "-").toLowerCase + ".html"
+
+val postInfo = BuildCtx.watchValue:
+  os.list(BuildCtx.workspaceRoot / "post")
+    .map: p =>
+      val s"$prefix - $suffix.md" = p.last
+      (prefix, suffix, p)
+    .sortBy(_(0).toInt)
+
+val bootstrapCss = link(
+  rel := "stylesheet",
+  href := "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
+)
+
+object post extends Cross[PostModule](postInfo.map(_(0)))
+trait PostModule extends Cross.Module[String]:
+  def number = crossValue
+  val Some((_, suffix, path)) = postInfo.find(_(0) == number)
+  def srcPath = Task.Source(path)
+  def render = Task:
+    val parser = org.commonmark.parser.Parser.builder().build()
+    val document = parser.parse(os.read(srcPath().path))
+    val renderer = org.commonmark.renderer.html.HtmlRenderer.builder().build()
+    val output = renderer.render(document)
+    os.write(
+      Task.dest /  mdNameToHtml(suffix),
+      doctype("html")(
+        html(
+          head(bootstrapCss),
+          body(
+            h1(a("Blog", href := "../index.html"), " / ", suffix),
+            raw(output)
+          )
+        )
+      )
+    )
+    PathRef(Task.dest / mdNameToHtml(suffix))
+
+def links = Task.Input { postInfo.map(_(1)) }
+val posts = Task.sequence(postInfo.map(_(0)).map(post(_).render))
+
+def index = Task:
+  os.write(
+    Task.dest / "index.html",
+    doctype("html")(
+      html(
+        head(bootstrapCss),
+        body(
+          h1("Blog"),
+          for suffix <- links()
+          yield h2(a(suffix, href := ("post/" + mdNameToHtml(suffix))))
+        )
+      )
+    )
+  )
+  PathRef(Task.dest / "index.html")
+
+def dist = Task:
+  for post <- posts() do
+    os.copy(post.path, Task.dest / "post" / post.path.last, createFolders = true)
+  os.copy(index().path, Task.dest / "index.html")
+  PathRef(Task.dest)
+
```
## Downstream Examples

- [10.7 - ExtendedBlog](https://github.com/handsonscala/handsonscala/tree/v2/examples/10.7%20-%20ExtendedBlog)