# Example 10.7 - ExtendedBlog
Adding previews and bundled Bootstrap CSS to our static blog build pipeline

```bash
./mill -i dist
```


## Upstream Example: [10.6 - Blog](https://github.com/handsonscala/handsonscala/tree/v1/examples/10.6%20-%20Blog):
Diff:
```diff
diff --git a/10.6 - Blog/build.sc b/10.7 - ExtendedBlog/build.sc
index 88b6b4d..f6174af 100644
--- a/10.6 - Blog/build.sc	
+++ b/10.7 - ExtendedBlog/build.sc	
@@ -13,61 +13,70 @@ val postInfo = interp.watchValue {
     .sortBy(_._1.toInt)
 }
 
-val bootstrapCss = link(
-  rel := "stylesheet",
-  href := "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
-)
+def bootstrap = T{
+  os.write(
+    T.dest / "bootstrap.css",
+    requests.get("https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css")
+      .text()
+  )
+  PathRef(T.dest / "bootstrap.css")
+}
 
-object post extends Cross[PostModule](postInfo.map(_._1):_*)
-class PostModule(number: String) extends Module{
-  val Some((_, suffix, path)) = postInfo.find(_._1 == number)
-  def srcPath = T.source(path)
-  def render = T{
+def renderMarkdown(s: String) = {
   val parser = org.commonmark.parser.Parser.builder().build()
-    val document = parser.parse(os.read(srcPath().path))
+  val document = parser.parse(s)
   val renderer = org.commonmark.renderer.html.HtmlRenderer.builder().build()
-    val output = renderer.render(document)
+  renderer.render(document)
+}
+def renderHtmlPage(dest: os.Path, bootstrapUrl: String, contents: Frag*) = {
   os.write(
-      T.dest /  mdNameToHtml(suffix),
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
+}
+
+object post extends Cross[PostModule](postInfo.map(_._1):_*)
+class PostModule(number: String) extends Module{
+  val Some((_, suffix, markdownPath)) = postInfo.find(_._1 == number)
+  def path = T.source(markdownPath)
+  def preview = T{
+    renderMarkdown(os.read.lines(path().path).takeWhile(_.nonEmpty).mkString("\n"))
+  }
+  def render = T{
+    renderHtmlPage(
+      T.dest / mdNameToHtml(suffix),
+      "../bootstrap.css",
+      h1(a(href := "../index.html")("Blog"), " / ", suffix),
+      raw(renderMarkdown(os.read(path().path)))
     )
-    PathRef(T.dest / mdNameToHtml(suffix))
   }
 }
 
 def links = T.input{ postInfo.map(_._2) }
+val posts = T.sequence(postInfo.map(_._1).map(post(_).render))
+val previews = T.sequence(postInfo.map(_._1).map(post(_).preview))
 
-def index = T{
-  os.write(
+def index = T {
+  renderHtmlPage(
     T.dest / "index.html",
-    doctype("html")(
-      html(
-        head(bootstrapCss),
-        body(
+    "bootstrap.css",
     h1("Blog"),
-          for (suffix <- links())
-          yield h2(a(suffix, href := ("post/" + mdNameToHtml(suffix))))
-        )
-      )
+    for ((suffix, preview) <- links().zip(previews()))
+    yield frag(
+      h2(a(href := ("post/" + mdNameToHtml(suffix)))(suffix)),
+      raw(preview) // include markdown-generated HTML "raw" without HTML-escaping it
     )
   )
-  PathRef(T.dest / "index.html")
 }
 
-val posts = T.sequence(postInfo.map(_._1).map(post(_).render))
-
 def dist = T {
   for (post <- posts()) {
     os.copy(post.path, T.dest / "post" / post.path.last, createFolders = true)
   }
   os.copy(index().path, T.dest / "index.html")
+  os.copy(bootstrap().path, T.dest / "bootstrap.css")
   PathRef(T.dest)
 }
```
## Downstream Examples

- [10.8 - Push](https://github.com/handsonscala/handsonscala/tree/v1/examples/10.8%20-%20Push)