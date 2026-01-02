# Example 7.8 - StreamingDownloadProcessReuploadTee
Five stage subprocess pipeline, `tee`d streaming data to a file

```bash
./mill -i StreamingDownloadProcessReupload.scala

ls -lh base64.gz
```


## Upstream Example: [7.6 - StreamingDownloadProcessReupload2](https://github.com/handsonscala/handsonscala/tree/v2/examples/7.6%20-%20StreamingDownloadProcessReupload2):
Diff:
```diff
diff --git a/7.6 - StreamingDownloadProcessReupload2/StreamingDownloadProcessReupload.scala b/7.8 - StreamingDownloadProcessReuploadTee/StreamingDownloadProcessReupload.scala
index 78373e9..6b00c01 100644
--- a/7.6 - StreamingDownloadProcessReupload2/StreamingDownloadProcessReupload.scala	
+++ b/7.8 - StreamingDownloadProcessReuploadTee/StreamingDownloadProcessReupload.scala	
@@ -2,9 +2,10 @@ def main() =
   val download = os.spawn(cmd = ("curl", "https://api.github.com/repos/com-lihaoyi/cask/releases"))
   val base64 = os.spawn(cmd = "base64", stdin = download.stdout)
   val gzip = os.spawn(cmd = "gzip", stdin = base64.stdout)
+  val tee = os.spawn(cmd = ("tee", "base64.gz"), stdin = gzip.stdout)
   val upload = os.spawn(
     cmd = ("curl", "-X", "PUT", "-d", "@-", "https://httpbin.org/anything"),
-    stdin = gzip.stdout
+    stdin = tee.stdout
   )
 
   val contentLength = upload.stdout.lines().filter(_.contains("Content-Length"))
```
