@ {
  val download = os
    .proc("curl", "https://api.github.com/repos/lihaoyi/mill/releases")
    .spawn()

  val upload = os
    .proc("curl", "-X", "PUT", "-d", "@-", "https://httpbin.org/anything")
    .spawn(stdin = download.stdout)

  val contentLength = upload.stdout.lines().filter(_.contains("Content-Length"))
  }
contentLength: Vector[String] = Vector("    \"Content-Length\": \"609216\", ")
