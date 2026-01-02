# Example 7.6 - StreamingDownloadProcessReupload2
Four-stage streaming subprocess pipeline

```bash
./mill -i StreamingDownloadProcessReupload.scala
```


## Upstream Example: [7.5 - StreamingDownloadProcessReupload1](https://github.com/handsonscala/handsonscala/tree/v2/examples/7.5%20-%20StreamingDownloadProcessReupload1):
Diff:
```diff
diff --git a/7.5 - StreamingDownloadProcessReupload1/StreamingDownloadProcessReupload.scala b/7.6 - StreamingDownloadProcessReupload2/StreamingDownloadProcessReupload.scala
index 5f40a39..78373e9 100644
--- a/7.5 - StreamingDownloadProcessReupload1/StreamingDownloadProcessReupload.scala	
+++ b/7.6 - StreamingDownloadProcessReupload2/StreamingDownloadProcessReupload.scala	
@@ -1,6 +1,7 @@
 def main() =
   val download = os.spawn(cmd = ("curl", "https://api.github.com/repos/com-lihaoyi/cask/releases"))
-  val gzip = os.spawn(cmd = "gzip", stdin = download.stdout)
+  val base64 = os.spawn(cmd = "base64", stdin = download.stdout)
+  val gzip = os.spawn(cmd = "gzip", stdin = base64.stdout)
   val upload = os.spawn(
     cmd = ("curl", "-X", "PUT", "-d", "@-", "https://httpbin.org/anything"),
     stdin = gzip.stdout
```
## Downstream Examples

- [7.8 - StreamingDownloadProcessReuploadTee](https://github.com/handsonscala/handsonscala/tree/v2/examples/7.8%20-%20StreamingDownloadProcessReuploadTee)