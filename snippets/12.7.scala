> val resp = requests.get(
    "https://api.github.com/repos/com-lihaoyi/upickle/issues",
    params = Map("state" -> "all"),
    headers = Map("Authorization" -> s"token $token")
  )
resp: requests.Response = Response(
  url = "https://api.github.com/repos/com-lihaoyi/upickle/issues",
  statusCode = 200,
  statusMessage = "OK",
  data = [{"url":"https://api.github.com/repos/com-lihaoyi/upickle/issues/687",...

> resp.text()
res4: String = "[{\"url\":\"https://.../com-lihaoyi/upickle/issues/620\"..."
