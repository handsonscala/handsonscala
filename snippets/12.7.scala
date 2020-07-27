@ val resp = requests.get(
    "https://api.github.com/repos/lihaoyi/upickle/issues",
    params = Map("state" -> "all"),
    headers = Map("Authorization" -> s"token $token")
  )
resp: requests.Response = Response(
  "https://api.github.com/repos/lihaoyi/upickle/issues",
  200,
  "OK",
  [{"url":"https://api.github.com/repos/lihaoyi/upickle/issues/294","repository_url": ...

@ resp.text()
res4: String = "[{\"url\":\"https://api.github.com/repos/lihaoyi/upickle/issues/620\",...
