> requests.post(
    "https://api.github.com/repos/lihaoyi/test/issues",
    data = ujson.Obj("title" -> "hello"),
    headers = Map("Authorization" -> s"token $token")
  )
res2: requests.Response = Response(
  url = "https://api.github.com/repos/lihaoyi/test/issues",
  statusCode = 201,
  statusMessage = "Created",
...
