# Example 16.6 - LoggingLongPipeline
Four-actor pipeline that logs sanitized, base64-encoded messages both to disk
and to `httpbin.org`

```bash
./mill -i TestLoggingPipeline.scala
```


## Upstream Example: [16.5 - LoggingPipeline](https://github.com/handsonscala/handsonscala/tree/v2/examples/16.5%20-%20LoggingPipeline):
Diff:
```diff
diff --git a/16.5 - LoggingPipeline/Classes.scala b/16.6 - LoggingLongPipeline/Classes.scala
index 224b580..ec37cc4 100644
--- a/16.5 - LoggingPipeline/Classes.scala	
+++ b/16.6 - LoggingLongPipeline/Classes.scala	
@@ -20,3 +20,14 @@ class Base64Actor(dest: castor.Actor[String])
                  (using cc: castor.Context) extends castor.SimpleActor[String]:
   def run(msg: String) =
     dest.send(java.util.Base64.getEncoder.encodeToString(msg.getBytes))
+
+class UploadActor(url: String)
+                 (using cc: castor.Context) extends castor.SimpleActor[String]:
+  def run(msg: String) =
+    val res = requests.post(url, data = msg)
+    println(s"response ${res.statusCode} " + ujson.read(res)("data"))
+
+class SanitizeActor(dest: castor.Actor[String])
+                   (using cc: castor.Context) extends castor.SimpleActor[String]:
+  def run(msg: String) =
+    dest.send(msg.replaceAll("([0-9]{4})[0-9]{8}([0-9]{4})", "<redacted>"))
diff --git a/16.5 - LoggingPipeline/LoggingPipeline.scala b/16.6 - LoggingLongPipeline/LoggingPipeline.scala
index 4b7f319..eca42fd 100644
--- a/16.5 - LoggingPipeline/LoggingPipeline.scala	
+++ b/16.6 - LoggingLongPipeline/LoggingPipeline.scala	
@@ -2,6 +2,8 @@
 given cc: castor.Context.Test()
 
 val diskActor = DiskActor(os.pwd / "log.txt")
-val base64Actor = Base64Actor(diskActor)
+val uploadActor = UploadActor("https://httpbin.org/post")
+val base64Actor = Base64Actor(castor.SplitActor(diskActor, uploadActor))
+val sanitizeActor = SanitizeActor(base64Actor)
 
-val logger = base64Actor
+val logger = sanitizeActor
diff --git a/16.5 - LoggingPipeline/TestLoggingPipeline.scala b/16.6 - LoggingLongPipeline/TestLoggingPipeline.scala
index 90ff1a3..b3c6331 100644
--- a/16.5 - LoggingPipeline/TestLoggingPipeline.scala	
+++ b/16.6 - LoggingLongPipeline/TestLoggingPipeline.scala	
@@ -6,7 +6,7 @@ def main() =
   logger.send("And I look good on the barbecue")
   logger.send("Yoghurt curds cream cheese and butter")
   logger.send("Comes from liquids from my udder")
-  logger.send("I am cow, I am cow")
+  logger.send("I am cow1234567887654321")
   logger.send("Hear me moo, moooo")
 
   // Logger hasn't finished yet, running in the background
@@ -14,8 +14,7 @@ def main() =
   // Now logger has finished
 
   def decodeFile(p: os.Path) =
-    os.read.lines(p).map(s => new String(java.util.Base64.getDecoder.decode(s)))
+    os.read.lines(p).map(s => String(java.util.Base64.getDecoder.decode(s)))
 
-  // When decoded, the contents are what we expect
   assert(decodeFile(os.pwd / "log.txt-old") == Seq("Comes from liquids from my udder"))
-  assert(decodeFile(os.pwd / "log.txt") == Seq("I am cow, I am cow", "Hear me moo, moooo"))
+  assert(decodeFile(os.pwd / "log.txt") == Seq("I am cow<redacted>", "Hear me moo, moooo"))
```
## Downstream Examples

- [16.7 - LoggingRearrangedPipeline1](https://github.com/handsonscala/handsonscala/tree/v2/examples/16.7%20-%20LoggingRearrangedPipeline1)
- [16.8 - LoggingRearrangedPipeline2](https://github.com/handsonscala/handsonscala/tree/v2/examples/16.8%20-%20LoggingRearrangedPipeline2)