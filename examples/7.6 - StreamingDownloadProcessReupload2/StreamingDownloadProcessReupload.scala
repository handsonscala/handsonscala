def main() =
  val download = os.spawn(cmd = ("curl", "https://api.github.com/repos/com-lihaoyi/cask/releases"))
  val base64 = os.spawn(cmd = "base64", stdin = download.stdout)
  val gzip = os.spawn(cmd = "gzip", stdin = base64.stdout)
  val upload = os.spawn(
    cmd = ("curl", "-X", "PUT", "-d", "@-", "https://httpbin.org/anything"),
    stdin = gzip.stdout
  )

  val contentLength = upload.stdout.lines().filter(_.contains("Content-Length"))
  pprint.log(contentLength)
