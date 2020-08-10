# Example 16.8 - LoggingRearrangedPipeline2
Four-actor pipeline, with only disk logs base64-encoded and only `httpbin.org`
uploads sanitized

```bash
amm TestLoggingPipeline.sc
```

## Upstream Example: [16.6 - LoggingLongPipeline](https://github.com/handsonscala/handsonscala/tree/v1/examples/16.6%20-%20LoggingLongPipeline):
Diff:
```diff
diff --git a/16.6 - LoggingLongPipeline/LoggingPipeline.sc b/16.8 - LoggingRearrangedPipeline2/LoggingPipeline.sc
index 8b8abf9..130c483 100644
--- a/16.6 - LoggingLongPipeline/LoggingPipeline.sc	
+++ b/16.8 - LoggingRearrangedPipeline2/LoggingPipeline.sc	
@@ -4,7 +4,8 @@ implicit val cc = new castor.Context.Test()
 
 val diskActor = new DiskActor(os.pwd / "log.txt")
 val uploadActor = new UploadActor("https://httpbin.org/post")
-val base64Actor = new Base64Actor(new castor.SplitActor(diskActor, uploadActor))
-val sanitizeActor = new SanitizeActor(base64Actor)
 
-val logger = sanitizeActor
+val base64Actor = new Base64Actor(diskActor)
+val sanitizeActor = new SanitizeActor(uploadActor)
+
+val logger = new castor.SplitActor(base64Actor, sanitizeActor)
diff --git a/16.6 - LoggingLongPipeline/TestLoggingPipeline.sc b/16.8 - LoggingRearrangedPipeline2/TestLoggingPipeline.sc
index 01f8c25..3a85264 100644
--- a/16.6 - LoggingLongPipeline/TestLoggingPipeline.sc	
+++ b/16.8 - LoggingRearrangedPipeline2/TestLoggingPipeline.sc	
@@ -17,5 +17,5 @@ def decodeFile(p: os.Path) = {
   os.read.lines(p).map(s => new String(java.util.Base64.getDecoder.decode(s)))
 }
 
-assert(decodeFile(os.pwd / "log.txt-old") == Seq("Comes from liquids from my udder"))
-assert(decodeFile(os.pwd / "log.txt") == Seq("I am cow<redacted>", "Hear me moo, moooo"))
+assert(decodeFile(os.pwd / "log.txt-old") == Seq("I am cow1234567887654321"))
+assert(decodeFile(os.pwd / "log.txt") == Seq("Hear me moo, moooo"))
```
