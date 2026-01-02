> {
  val url = "https://api.github.com/repos/com-lihaoyi/cask/releases"
  val download = os.spawn(cmd = ("curl", "-L", url))

  val upload = os.spawn(
    cmd = ("curl", "-X", "PUT", "-d", "@-", "https://httpbin.org/anything"),
    stdin = download.stdout
  )

  val contentLength = upload.stdout.lines().filter(_.contains("Content-Length"))
  }
contentLength: Vector[String] = Vector(
  "    \"Content-Length\": \"1759343\", "
)
