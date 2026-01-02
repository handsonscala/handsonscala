# Example 10.10 - ConcatPdf
Static blog pipeline which can generate PDFs for each blog post and concatenate
them using Apache PDFBox

```bash
./mill -i pdfs
./mill -i combinedPdf
ls out/pdfs.dest | grep '\.pdf'
ls out/combinedPdf.dest | grep '\.pdf'
```


## Upstream Example: [10.9 - PostPdf](https://github.com/handsonscala/handsonscala/tree/v2/examples/10.9%20-%20PostPdf):
Diff:
```diff
diff --git a/10.9 - PostPdf/build.mill b/10.10 - ConcatPdf/build.mill
index 164d44b..fa33932 100644
--- a/10.9 - PostPdf/build.mill	
+++ b/10.10 - ConcatPdf/build.mill	
@@ -66,7 +66,6 @@ trait PostModule extends Cross.Module[String]:
     for p <- os.list(puppeteer().path) do os.copy.over(p, Task.dest / p.last)
     os.copy(bootstrap().path, Task.dest / "bootstrap.css")
     os.makeDir(Task.dest / "post")
-
     val htmlPath = Task.dest / "post" / render().path.last
     os.copy(render().path, htmlPath)
 
@@ -78,7 +77,6 @@ trait PostModule extends Cross.Module[String]:
     os.call(cmd = ("node", localPdfize, htmlPath, pdfPath), cwd = Task.dest)
     PathRef(pdfPath)
 
-
 def links = Task.Input{ postInfo.map(_(1)) }
 val posts = Task.sequence(postInfo.map(_(0)).map(post(_).render))
 val previews = Task.sequence(postInfo.map(_(0)).map(post(_).preview))
@@ -95,7 +93,7 @@ def index = Task:
     )
   )
 
-def dist = Task:
+def dist = Task :
   for post <- posts() do
     os.copy(post.path, Task.dest / "post" / post.path.last, createFolders = true)
 
@@ -108,3 +106,16 @@ val pdfFiles = Task.sequence(postInfo.map(_(0)).map(post(_).pdf))
 def pdfs = Task:
   for pdf <- pdfFiles() do os.copy.into(pdf.path, Task.dest)
   PathRef(Task.dest)
+
+def combinedPdf = Task:
+  val outPath = Task.dest / "combined.pdf"
+  val merger = org.apache.pdfbox.multipdf.PDFMergerUtility()
+  for pdf <- pdfFiles() do merger.addSource(pdf.path.toIO)
+  val out = os.write.outputStream(outPath)
+  try
+    merger.setDestinationStream(out)
+    merger.mergeDocuments()
+  finally
+    out.close()
+  PathRef(outPath)
+
```
