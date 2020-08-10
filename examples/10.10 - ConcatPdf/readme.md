# Example 10.10 - ConcatPdf
Static blog pipeline which can generate PDFs for each blog post and concatenate
them using Apache PDFBox

```bash
./mill -i pdfs
./mill -i combinedPdf
ls out/pdfs/dest | grep '\.pdf'
ls out/combinedPdf/dest | grep '\.pdf'
```


## Upstream Example: [10.9 - PostPdf](https://github.com/handsonscala/handsonscala/tree/v1/examples/10.9%20-%20PostPdf):
Diff:
```diff
diff --git a/10.9 - PostPdf/build.sc b/10.10 - ConcatPdf/build.sc
index f7cfcef..3dca7e1 100644
--- a/10.9 - PostPdf/build.sc	
+++ b/10.10 - ConcatPdf/build.sc	
@@ -1,5 +1,6 @@
 import $ivy.`com.lihaoyi::scalatags:0.9.1`, scalatags.Text.all._
 import $ivy.`com.atlassian.commonmark:commonmark:0.13.1`
+import $ivy.`org.apache.pdfbox:pdfbox:2.0.18`
 import mill._
 
 def mdNameToHtml(name: String) = name.replace(" ", "-").toLowerCase + ".html"
@@ -103,3 +104,14 @@ def pdfs = T {
   for (pdf <- pdfFiles()) os.copy.into(pdf.path, T.dest)
   PathRef(T.dest)
 }
+def combinedPdf = T{
+  val outPath = T.dest / "combined.pdf"
+  val merger = new org.apache.pdfbox.multipdf.PDFMergerUtility
+  for (pdf <- pdfFiles()) merger.addSource(pdf.path.toIO)
+  val out = os.write.outputStream(outPath)
+  try {
+    merger.setDestinationStream(out)
+    merger.mergeDocuments()
+  } finally out.close()
+  PathRef(outPath)
+}
```
