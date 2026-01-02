 class DiskActor...
+
+class Base64Actor(dest: castor.Actor[String])
+                 (using cc: castor.Context) extends castor.SimpleActor[String]:
+  def run(msg: String) =
+    dest.send(java.util.Base64.getEncoder.encodeToString(msg.getBytes))