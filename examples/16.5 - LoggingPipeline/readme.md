# Example 16.5 - LoggingPipeline
Two-stage logging actor pipeline that asynchronously logs base64-encoded
messages to disk

```bash
./mill -i TestLoggingPipeline.scala
```

## Upstream Example: [16.4 - LoggingSimple](https://github.com/handsonscala/handsonscala/tree/v2/examples/16.4%20-%20LoggingSimple):
Diff:
```diff
diff --git a/16.4 - LoggingSimple/Classes.scala b/16.5 - LoggingPipeline/Classes.scala
index bd27969..224b580 100644
--- a/16.4 - LoggingSimple/Classes.scala	
+++ b/16.5 - LoggingPipeline/Classes.scala	
@@ -1,6 +1,5 @@
 //| mvnDeps:
 //| - com.lihaoyi::castor:0.3.0
-
 class DiskActor(logPath: os.Path, rotateSize: Int = 50)
                (using cc: castor.Context) extends castor.SimpleActor[String]:
   val oldPath = logPath / os.up / (logPath.last + "-old")
@@ -16,3 +15,8 @@ class DiskActor(logPath: os.Path, rotateSize: Int = 50)
     os.write.append(logPath, s + "\n", createFolders = true)
   
   private var logSize = 0
+
+class Base64Actor(dest: castor.Actor[String])
+                 (using cc: castor.Context) extends castor.SimpleActor[String]:
+  def run(msg: String) =
+    dest.send(java.util.Base64.getEncoder.encodeToString(msg.getBytes))
diff --git a/16.4 - LoggingSimple/LoggingPipeline.scala b/16.5 - LoggingPipeline/LoggingPipeline.scala
index ccf3277..4b7f319 100644
--- a/16.4 - LoggingSimple/LoggingPipeline.scala	
+++ b/16.5 - LoggingPipeline/LoggingPipeline.scala	
@@ -2,5 +2,6 @@
 given cc: castor.Context.Test()
 
 val diskActor = DiskActor(os.pwd / "log.txt")
+val base64Actor = Base64Actor(diskActor)
 
-val logger = diskActor
+val logger = base64Actor
diff --git a/16.4 - LoggingSimple/TestLoggingPipeline.scala b/16.5 - LoggingPipeline/TestLoggingPipeline.scala
index adcad41..90ff1a3 100644
--- a/16.4 - LoggingSimple/TestLoggingPipeline.scala	
+++ b/16.5 - LoggingPipeline/TestLoggingPipeline.scala	
@@ -13,8 +13,9 @@ def main() =
   cc.waitForInactivity()
   // Now logger has finished
 
-  assert(os.read.lines(os.pwd / "log.txt-old") == Seq("Comes from liquids from my udder"))
-  assert(
-    os.read.lines(os.pwd / "log.txt") ==
-    Seq("I am cow, I am cow", "Hear me moo, moooo")
-  )
+  def decodeFile(p: os.Path) =
+    os.read.lines(p).map(s => new String(java.util.Base64.getDecoder.decode(s)))
+
+  // When decoded, the contents are what we expect
+  assert(decodeFile(os.pwd / "log.txt-old") == Seq("Comes from liquids from my udder"))
+  assert(decodeFile(os.pwd / "log.txt") == Seq("I am cow, I am cow", "Hear me moo, moooo"))
```
## Downstream Examples

- [16.6 - LoggingLongPipeline](https://github.com/handsonscala/handsonscala/tree/v2/examples/16.6%20-%20LoggingLongPipeline)