# Example 10.7 - ExtendedBlog
Adding previews and bundled Bootstrap CSS to our static blog build pipeline

```bash
./mill -i dist
```


## Upstream Example: [10.6 - Blog](https://github.com/handsonscala/handsonscala/tree/v2/examples/10.6%20-%20Blog):
Diff:
```diff
diff --git a/10.6 - Blog/build.mill b/10.7 - ExtendedBlog/build.mill
index 30a5e36..055c61a 100644
--- a/10.6 - Blog/build.mill	
+++ b/10.7 - ExtendedBlog/build.mill	
@@ -15,57 +15,66 @@ val postInfo = BuildCtx.watchValue:
       (prefix, suffix, p)
     .sortBy(_(0).toInt)
 
-val bootstrapCss = link(
-  rel := "stylesheet",
-  href := "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
-)
+def bootstrap = Task:
+  os.write(
+    Task.dest / "bootstrap.css",
+    requests.get(
+      "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
+    )
+  )
+  PathRef(Task.dest / "bootstrap.css")
 
-object post extends Cross[PostModule](postInfo.map(_(0)))
-trait PostModule extends Cross.Module[String]:
-  def number = crossValue
-  val Some((_, suffix, path)) = postInfo.find(_(0) == number)
-  def srcPath = Task.Source(path)
-  def render = Task:
+def renderMarkdown(s: String) =
   val parser = org.commonmark.parser.Parser.builder().build()
-    val document = parser.parse(os.read(srcPath().path))
+  val document = parser.parse(s)
   val renderer = org.commonmark.renderer.html.HtmlRenderer.builder().build()
-    val output = renderer.render(document)
+  renderer.render(document)
+
+def renderHtmlPage(dest: os.Path, bootstrapUrl: String, contents: Frag*) =
   os.write(
-      Task.dest /  mdNameToHtml(suffix),
+    dest,
     doctype("html")(
-        html(
-          head(bootstrapCss),
-          body(
-            h1(a("Blog", href := "../index.html"), " / ", suffix),
-            raw(output)
-          )
+      html(head(link(rel := "stylesheet", href := bootstrapUrl)), body(contents))
     )
   )
+  PathRef(dest)
+
+object post extends Cross[PostModule](postInfo.map(_(0)))
+trait PostModule extends Cross.Module[String]:
+  def number = crossValue
+  val Some((_, suffix, markdownPath)) = postInfo.find(_(0) == number)
+  def path = Task.Source(markdownPath)
+  def preview = Task:
+    renderMarkdown(os.read.lines(path().path).takeWhile(_.nonEmpty).mkString("\n"))
+
+  def render = Task:
+    renderHtmlPage(
+      Task.dest / mdNameToHtml(suffix),
+      "../bootstrap.css",
+      h1(a(href := "../index.html")("Blog"), " / ", suffix),
+      raw(renderMarkdown(os.read(path().path)))
     )
-    PathRef(Task.dest / mdNameToHtml(suffix))
 
-def links = Task.Input { postInfo.map(_(1)) }
+def links = Task.Input{ postInfo.map(_(1)) }
 val posts = Task.sequence(postInfo.map(_(0)).map(post(_).render))
+val previews = Task.sequence(postInfo.map(_(0)).map(post(_).preview))
 
 def index = Task:
-  os.write(
+  renderHtmlPage(
     Task.dest / "index.html",
-    doctype("html")(
-      html(
-        head(bootstrapCss),
-        body(
+    "bootstrap.css",
     h1("Blog"),
-          for suffix <- links()
-          yield h2(a(suffix, href := ("post/" + mdNameToHtml(suffix))))
-        )
-      )
+    for (suffix, preview) <- links().zip(previews())
+    yield frag(
+      h2(a(href := ("post/" + mdNameToHtml(suffix)))(suffix)),
+      raw(preview) // include markdown-generated HTML "raw" without HTML-escaping
     )
   )
-  PathRef(Task.dest / "index.html")
 
 def dist = Task:
   for post <- posts() do
     os.copy(post.path, Task.dest / "post" / post.path.last, createFolders = true)
+
   os.copy(index().path, Task.dest / "index.html")
+  os.copy(bootstrap().path, Task.dest / "bootstrap.css")
   PathRef(Task.dest)
-
```
## Downstream Examples

- [10.8 - Push](https://github.com/handsonscala/handsonscala/tree/v2/examples/10.8%20-%20Push)
- [10.9 - PostPdf](https://github.com/handsonscala/handsonscala/tree/v2/examples/10.9%20-%20PostPdf)