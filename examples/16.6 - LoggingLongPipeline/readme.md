# Example 16.6 - LoggingLongPipeline
Four-actor pipeline that logs sanitized, base64-encoded messages both to disk
and to `httpbin.org`

```bash
amm TestLoggingPipeline.sc
```


## Upstream Example: [16.5 - LoggingPipeline](https://github.com/handsonscala/handsonscala/tree/v1/examples/16.5%20-%20LoggingPipeline):
Diff:
```diff
diff --git a/16.5 - LoggingPipeline/Classes.sc b/16.6 - LoggingLongPipeline/Classes.sc
index 8a75f81..55edea2 100644
--- a/16.5 - LoggingPipeline/Classes.sc	
+++ b/16.6 - LoggingLongPipeline/Classes.sc	
@@ -20,3 +20,17 @@ class Base64Actor(dest: castor.Actor[String])
     dest.send(java.util.Base64.getEncoder.encodeToString(msg.getBytes))
   }
 }
+
+class UploadActor(url: String)
+                 (implicit cc: castor.Context) extends castor.SimpleActor[String]{
+  def run(msg: String) = {
+    val res = requests.post(url, data = msg)
+    println(s"response ${res.statusCode} " + ujson.read(res.text())("data"))
+  }
+}
+class SanitizeActor(dest: castor.Actor[String])
+                   (implicit cc: castor.Context) extends castor.SimpleActor[String]{
+  def run(msg: String) = {
+    dest.send(msg.replaceAll("([0-9]{4})[0-9]{8}([0-9]{4})", "<redacted>"))
+  }
+}
diff --git a/16.5 - LoggingPipeline/LoggingPipeline.sc b/16.6 - LoggingLongPipeline/LoggingPipeline.sc
index f01c26a..8b8abf9 100644
--- a/16.5 - LoggingPipeline/LoggingPipeline.sc	
+++ b/16.6 - LoggingLongPipeline/LoggingPipeline.sc	
@@ -3,6 +3,8 @@ import $file.Classes, Classes._
 implicit val cc = new castor.Context.Test()
 
 val diskActor = new DiskActor(os.pwd / "log.txt")
-val base64Actor = new Base64Actor(diskActor)
+val uploadActor = new UploadActor("https://httpbin.org/post")
+val base64Actor = new Base64Actor(new castor.SplitActor(diskActor, uploadActor))
+val sanitizeActor = new SanitizeActor(base64Actor)
 
-val logger = base64Actor
+val logger = sanitizeActor
diff --git a/16.5 - LoggingPipeline/TestLoggingPipeline.sc b/16.6 - LoggingLongPipeline/TestLoggingPipeline.sc
index 1df7704..01f8c25 100644
--- a/16.5 - LoggingPipeline/TestLoggingPipeline.sc	
+++ b/16.6 - LoggingLongPipeline/TestLoggingPipeline.sc	
@@ -6,7 +6,7 @@ logger.send("I weight twice as much as you")
 logger.send("And I look good on the barbecue")
 logger.send("Yoghurt curds cream cheese and butter")
 logger.send("Comes from liquids from my udder")
-logger.send("I am cow, I am cow")
+logger.send("I am cow1234567887654321")
 logger.send("Hear me moo, moooo")
 
 // Logger hasn't finished yet, running in the background
@@ -16,6 +16,6 @@ cc.waitForInactivity()
 def decodeFile(p: os.Path) = {
   os.read.lines(p).map(s => new String(java.util.Base64.getDecoder.decode(s)))
 }
-// When decoded, the contents are what we expect
+
 assert(decodeFile(os.pwd / "log.txt-old") == Seq("Comes from liquids from my udder"))
-assert(decodeFile(os.pwd / "log.txt") == Seq("I am cow, I am cow", "Hear me moo, moooo"))
+assert(decodeFile(os.pwd / "log.txt") == Seq("I am cow<redacted>", "Hear me moo, moooo"))
```
## Downstream Examples

- [16.7 - LoggingRearrangedPipeline1](https://github.com/handsonscala/handsonscala/tree/v1/examples/16.7%20-%20LoggingRearrangedPipeline1)
- [16.8 - LoggingRearrangedPipeline2](https://github.com/handsonscala/handsonscala/tree/v1/examples/16.8%20-%20LoggingRearrangedPipeline2)