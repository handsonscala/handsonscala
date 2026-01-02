# Example 16.7 - LoggingRearrangedPipeline1
Three-actor logging pipeline without sanitization

```bash
./mill -i TestLoggingPipeline.scala
```


## Upstream Example: [16.6 - LoggingLongPipeline](https://github.com/handsonscala/handsonscala/tree/v2/examples/16.6%20-%20LoggingLongPipeline):
Diff:
```diff
diff --git a/16.6 - LoggingLongPipeline/LoggingPipeline.scala b/16.7 - LoggingRearrangedPipeline1/LoggingPipeline.scala
index eca42fd..95c67f3 100644
--- a/16.6 - LoggingLongPipeline/LoggingPipeline.scala	
+++ b/16.7 - LoggingRearrangedPipeline1/LoggingPipeline.scala	
@@ -1,9 +1,8 @@
 //| moduleDeps: [Classes.scala]
 given cc: castor.Context.Test()
 
-val diskActor = DiskActor(os.pwd / "log.txt")
-val uploadActor = UploadActor("https://httpbin.org/post")
-val base64Actor = Base64Actor(castor.SplitActor(diskActor, uploadActor))
-val sanitizeActor = SanitizeActor(base64Actor)
+val diskActor = new DiskActor(os.pwd / "log.txt")
+val uploadActor = new UploadActor("https://httpbin.org/post")
+val base64Actor = new Base64Actor(new castor.SplitActor(diskActor, uploadActor))
 
-val logger = sanitizeActor
+val logger = base64Actor
diff --git a/16.6 - LoggingLongPipeline/TestLoggingPipeline.scala b/16.7 - LoggingRearrangedPipeline1/TestLoggingPipeline.scala
index b3c6331..bb7ebf7 100644
--- a/16.6 - LoggingLongPipeline/TestLoggingPipeline.scala	
+++ b/16.7 - LoggingRearrangedPipeline1/TestLoggingPipeline.scala	
@@ -14,7 +14,8 @@ def main() =
   // Now logger has finished
 
   def decodeFile(p: os.Path) =
-    os.read.lines(p).map(s => String(java.util.Base64.getDecoder.decode(s)))
+    os.read.lines(p).map(s => new String(java.util.Base64.getDecoder.decode(s)))
 
-  assert(decodeFile(os.pwd / "log.txt-old") == Seq("Comes from liquids from my udder"))
-  assert(decodeFile(os.pwd / "log.txt") == Seq("I am cow<redacted>", "Hear me moo, moooo"))
+  println(decodeFile(os.pwd / "log.txt-old"))
+  assert(decodeFile(os.pwd / "log.txt-old") == Seq("I am cow1234567887654321"))
+  assert(decodeFile(os.pwd / "log.txt") == Seq("Hear me moo, moooo"))
```
