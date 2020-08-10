 @ {
   val download = ...

+  val base64 = os.proc("base64").spawn(stdin = download.stdout)
+
+  val gzip = os.proc("gzip").spawn(stdin = base64.stdout)

   val upload = os
     .proc("curl", "-X", "PUT", "-d", "@-", "https://httpbin.org/anything")
-    .spawn(stdin = download.stdout)
+    .spawn(stdin = gzip.stdout)

   val contentLength = upload.stdout.lines().filter(_.contains("Content-Length"))
   }
 contentLength: Vector[String] = Vector("    \"Content-Length\": \"38517\", ")
