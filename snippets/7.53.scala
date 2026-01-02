 > {
   val url = ...
   val download = ...

+  val base64 = os.spawn(cmd = "base64", stdin = download.stdout)
+  val gzip = os.spawn(cmd = "gzip", stdin = base64.stdout)
   val upload = os.spawn(
     cmd = ("curl", "-X", "PUT", "-d", "@-", "https://httpbin.org/anything"),
-    stdin = download.stdout
+    stdin = gzip.stdout
   )

   val contentLength = upload.stdout.lines().filter(_.contains("Content-Length"))
   }
 contentLength: Vector[String] = Vector("    \"Content-Length\": \"201864\", ")
