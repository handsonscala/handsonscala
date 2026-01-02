# Example 16.8 - LoggingRearrangedPipeline2
Four-actor pipeline, with only disk logs base64-encoded and only `httpbin.org`
uploads sanitized

```bash
./mill -i TestLoggingPipeline.scala
```

## Upstream Example: [16.6 - LoggingLongPipeline](https://github.com/handsonscala/handsonscala/tree/v2/examples/16.6%20-%20LoggingLongPipeline):
Diff:
```diff
diff --git a/16.6 - LoggingLongPipeline/LoggingPipeline.scala b/16.8 - LoggingRearrangedPipeline2/LoggingPipeline.scala
index eca42fd..9a4b500 100644
--- a/16.6 - LoggingLongPipeline/LoggingPipeline.scala	
+++ b/16.8 - LoggingRearrangedPipeline2/LoggingPipeline.scala	
@@ -3,7 +3,8 @@ given cc: castor.Context.Test()
 
 val diskActor = DiskActor(os.pwd / "log.txt")
 val uploadActor = UploadActor("https://httpbin.org/post")
-val base64Actor = Base64Actor(castor.SplitActor(diskActor, uploadActor))
-val sanitizeActor = SanitizeActor(base64Actor)
 
-val logger = sanitizeActor
+val base64Actor = Base64Actor(diskActor)
+val sanitizeActor = SanitizeActor(uploadActor)
+
+val logger = castor.SplitActor(base64Actor, sanitizeActor)
diff --git a/16.6 - LoggingLongPipeline/TestLoggingPipeline.scala b/16.8 - LoggingRearrangedPipeline2/TestLoggingPipeline.scala
index b3c6331..04ca685 100644
--- a/16.6 - LoggingLongPipeline/TestLoggingPipeline.scala	
+++ b/16.8 - LoggingRearrangedPipeline2/TestLoggingPipeline.scala	
@@ -14,7 +14,7 @@ def main() =
   // Now logger has finished
 
   def decodeFile(p: os.Path) =
-    os.read.lines(p).map(s => String(java.util.Base64.getDecoder.decode(s)))
+    os.read.lines(p).map(s => new String(java.util.Base64.getDecoder.decode(s)))
 
-  assert(decodeFile(os.pwd / "log.txt-old") == Seq("Comes from liquids from my udder"))
-  assert(decodeFile(os.pwd / "log.txt") == Seq("I am cow<redacted>", "Hear me moo, moooo"))
+  assert(decodeFile(os.pwd / "log.txt-old") == Seq("I am cow1234567887654321"))
+  assert(decodeFile(os.pwd / "log.txt") == Seq("Hear me moo, moooo"))
```
