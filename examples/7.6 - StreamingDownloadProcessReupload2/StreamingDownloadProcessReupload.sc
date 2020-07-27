val download = os.proc( "curl", "https://api.github.com/repos/lihaoyi/mill/releases").spawn()
val base64 = os.proc("base64").spawn(stdin = download.stdout)
val gzip = os.proc("gzip").spawn(stdin = base64.stdout)
val upload = os
  .proc("curl", "-X", "PUT", "-d", "@-", "https://httpbin.org/anything")
  .spawn(stdin = gzip.stdout)

val contentLength = upload.stdout.lines.filter(_.contains("Content-Length"))
pprint.log(contentLength)
