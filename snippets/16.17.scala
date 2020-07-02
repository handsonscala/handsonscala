 import $ivy.`com.lihaoyi::castor:0.1.3`
 class DiskActor...

+class Base64Actor(dest: castor.Actor[String])
+                 (implicit cc: castor.Context) extends castor.SimpleActor[String]{
+  def run(msg: String) = {
+    dest.send(java.util.Base64.getEncoder.encodeToString(msg.getBytes))
+  }
+}