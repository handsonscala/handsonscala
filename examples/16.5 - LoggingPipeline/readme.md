# Example 16.5 - LoggingPipeline
Two-stage logging actor pipeline that asynchronously logs base64-encoded
messages to disk

```bash
amm TestLoggingPipeline.sc
```

## Upstream Example: [16.4 - LoggingSimple](https://github.com/handsonscala/handsonscala/tree/v1/examples/16.4%20-%20LoggingSimple):
Diff:
```diff
diff --git a/16.4 - LoggingSimple/Classes.sc b/16.5 - LoggingPipeline/Classes.sc
index 12bd6ad..8a75f81 100644
--- a/16.4 - LoggingSimple/Classes.sc	
+++ b/16.5 - LoggingPipeline/Classes.sc	
@@ -13,3 +13,10 @@ class DiskActor(logPath: os.Path, rotateSize: Int = 50)
   }
   private var logSize = 0
 }
+
+class Base64Actor(dest: castor.Actor[String])
+                 (implicit cc: castor.Context) extends castor.SimpleActor[String]{
+  def run(msg: String) = {
+    dest.send(java.util.Base64.getEncoder.encodeToString(msg.getBytes))
+  }
+}
diff --git a/16.4 - LoggingSimple/LoggingPipeline.sc b/16.5 - LoggingPipeline/LoggingPipeline.sc
index a962d6e..f01c26a 100644
--- a/16.4 - LoggingSimple/LoggingPipeline.sc	
+++ b/16.5 - LoggingPipeline/LoggingPipeline.sc	
@@ -3,5 +3,6 @@ import $file.Classes, Classes._
 implicit val cc = new castor.Context.Test()
 
 val diskActor = new DiskActor(os.pwd / "log.txt")
+val base64Actor = new Base64Actor(diskActor)
 
-val logger = diskActor
+val logger = base64Actor
diff --git a/16.4 - LoggingSimple/TestLoggingPipeline.sc b/16.5 - LoggingPipeline/TestLoggingPipeline.sc
index ae45d7d..1df7704 100644
--- a/16.4 - LoggingSimple/TestLoggingPipeline.sc	
+++ b/16.5 - LoggingPipeline/TestLoggingPipeline.sc	
@@ -13,8 +13,9 @@ logger.send("Hear me moo, moooo")
 cc.waitForInactivity()
 // Now logger has finished
 
-assert(os.read.lines(os.pwd / "log.txt-old") == Seq("Comes from liquids from my udder"))
-assert(
-  os.read.lines(os.pwd / "log.txt") ==
-  Seq("I am cow, I am cow", "Hear me moo, moooo")
-)
+def decodeFile(p: os.Path) = {
+  os.read.lines(p).map(s => new String(java.util.Base64.getDecoder.decode(s)))
+}
+// When decoded, the contents are what we expect
+assert(decodeFile(os.pwd / "log.txt-old") == Seq("Comes from liquids from my udder"))
+assert(decodeFile(os.pwd / "log.txt") == Seq("I am cow, I am cow", "Hear me moo, moooo"))
```
## Downstream Examples

- [16.6 - LoggingLongPipeline](https://github.com/handsonscala/handsonscala/tree/v1/examples/16.6%20-%20LoggingLongPipeline)