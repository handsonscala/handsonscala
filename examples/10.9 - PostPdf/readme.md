# Example 10.9 - PostPdf
Static blog pipeline which can generate PDFs for each blog post using Puppeteer

```bash
./mill -i pdfs
ls out/pdfs.dest | grep '\.pdf'
```


## Upstream Example: [10.7 - ExtendedBlog](https://github.com/handsonscala/handsonscala/tree/v2/examples/10.7%20-%20ExtendedBlog):
Diff:
```diff
diff --git a/10.7 - ExtendedBlog/build.mill b/10.9 - PostPdf/build.mill
index 055c61a..164d44b 100644
--- a/10.7 - ExtendedBlog/build.mill	
+++ b/10.9 - PostPdf/build.mill	
@@ -1,6 +1,7 @@
 //| mvnDeps:
 //| - com.lihaoyi::scalatags:0.13.1
 //| - org.commonmark:commonmark:0.26.0
+//| - org.apache.pdfbox:pdfbox:2.0.18
 import mill.*
 import mill.api.BuildCtx
 import scalatags.Text.all.*
@@ -39,6 +40,12 @@ def renderHtmlPage(dest: os.Path, bootstrapUrl: String, contents: Frag*) =
   )
   PathRef(dest)
 
+def puppeteer = Task:
+  os.call(cmd = ("npm", "install", "puppeteer@24.24.1"), cwd = Task.dest, stderr = os.Pipe)
+  PathRef(Task.dest)
+
+def pdfize = Task.Source("pdfize.js")
+
 object post extends Cross[PostModule](postInfo.map(_(0)))
 trait PostModule extends Cross.Module[String]:
   def number = crossValue
@@ -55,6 +62,23 @@ trait PostModule extends Cross.Module[String]:
       raw(renderMarkdown(os.read(path().path)))
     )
 
+  def pdf = Task:
+    for p <- os.list(puppeteer().path) do os.copy.over(p, Task.dest / p.last)
+    os.copy(bootstrap().path, Task.dest / "bootstrap.css")
+    os.makeDir(Task.dest / "post")
+
+    val htmlPath = Task.dest / "post" / render().path.last
+    os.copy(render().path, htmlPath)
+
+    val localPdfize = Task.dest / pdfize().path.last
+
+    os.copy.over(pdfize().path, localPdfize)
+    val s"$baseName.html" = htmlPath.last
+    val pdfPath = Task.dest / s"$baseName.pdf"
+    os.call(cmd = ("node", localPdfize, htmlPath, pdfPath), cwd = Task.dest)
+    PathRef(pdfPath)
+
+
 def links = Task.Input{ postInfo.map(_(1)) }
 val posts = Task.sequence(postInfo.map(_(0)).map(post(_).render))
 val previews = Task.sequence(postInfo.map(_(0)).map(post(_).preview))
@@ -78,3 +102,9 @@ def dist = Task:
   os.copy(index().path, Task.dest / "index.html")
   os.copy(bootstrap().path, Task.dest / "bootstrap.css")
   PathRef(Task.dest)
+
+val pdfFiles = Task.sequence(postInfo.map(_(0)).map(post(_).pdf))
+
+def pdfs = Task:
+  for pdf <- pdfFiles() do os.copy.into(pdf.path, Task.dest)
+  PathRef(Task.dest)
diff --git a/10.9 - PostPdf/pdfize.js b/10.9 - PostPdf/pdfize.js
new file mode 100644
index 0000000..aa3863f
--- /dev/null
+++ b/10.9 - PostPdf/pdfize.js	
@@ -0,0 +1,8 @@
+const puppeteer = require('puppeteer');
+const [src, dest] = process.argv.slice(2)
+puppeteer.launch().then(async function(browser){
+  const page = await browser.newPage();
+  await page.goto("file://" + src, {waitUntil: 'load'});
+  await page.pdf({path: dest, format: 'A4'});
+  process.exit(0)
+})
```
## Downstream Examples

- [10.10 - ConcatPdf](https://github.com/handsonscala/handsonscala/tree/v2/examples/10.10%20-%20ConcatPdf)