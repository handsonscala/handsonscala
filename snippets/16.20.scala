 class DiskActor...
 class Base64Actor...
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