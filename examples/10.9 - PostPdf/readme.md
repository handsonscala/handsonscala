# Example 10.9 - PostPdf
Static blog pipeline which can generate PDFs for each blog post using Puppeteer

```bash
./mill -i pdfs
ls out/pdfs/dest | grep '\.pdf'
```


## Upstream Example: [10.7 - ExtendedBlog](https://github.com/handsonscala/handsonscala/tree/v1/examples/10.7%20-%20ExtendedBlog):
Diff:
```diff
diff --git a/10.7 - ExtendedBlog/build.sc b/10.9 - PostPdf/build.sc
index f6174af..650974d 100644
--- a/10.7 - ExtendedBlog/build.sc	
+++ b/10.9 - PostPdf/build.sc	
@@ -38,6 +38,13 @@ def renderHtmlPage(dest: os.Path, bootstrapUrl: String, contents: Frag*) = {
   PathRef(dest)
 }
 
+def puppeteer = T{
+  os.proc("npm", "install", "puppeteer").call(cwd = T.dest, stderr = os.Pipe)
+  PathRef(T.dest)
+}
+
+def pdfize = T.source(os.pwd / "pdfize.js")
+
 object post extends Cross[PostModule](postInfo.map(_._1):_*)
 class PostModule(number: String) extends Module{
   val Some((_, suffix, markdownPath)) = postInfo.find(_._1 == number)
@@ -53,6 +60,24 @@ class PostModule(number: String) extends Module{
       raw(renderMarkdown(os.read(path().path)))
     )
   }
+
+  def pdf = T {
+    for(p <- os.list(puppeteer().path)) os.copy.over(p, T.dest / p.last)
+    os.copy(bootstrap().path, T.dest / "bootstrap.css")
+    os.makeDir(T.dest / "post")
+    val htmlPath = T.dest / "post" / render().path.last
+    os.copy(render().path, htmlPath)
+
+    val localPdfize = T.dest / pdfize().path.last
+
+    os.copy.over(pdfize().path, localPdfize)
+    val s"$baseName.html" = htmlPath.last
+    val pdfPath = T.dest / s"$baseName.pdf"
+    os.proc("node", localPdfize, htmlPath, pdfPath)
+      .call(cwd = T.dest)
+    PathRef(pdfPath)
+  }
+
 }
 
 def links = T.input{ postInfo.map(_._2) }
@@ -80,3 +105,9 @@ def dist = T {
   os.copy(bootstrap().path, T.dest / "bootstrap.css")
   PathRef(T.dest)
 }
+
+val pdfFiles = T.sequence(postInfo.map(_._1).map(post(_).pdf))
+def pdfs = T {
+  for (pdf <- pdfFiles()) os.copy.into(pdf.path, T.dest)
+  PathRef(T.dest)
+}
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

- [10.10 - ConcatPdf](https://github.com/handsonscala/handsonscala/tree/v1/examples/10.10%20-%20ConcatPdf)