@ requests.post(
    "https://api.github.com/repos/lihaoyi/test/issues",
    data = ujson.Obj("title" -> "hello"),
    headers = Map("Authorization" -> s"token $token")
  )
res1: requests.Response = Response(
  "https://api.github.com/repos/lihaoyi/test/issues",
  201,
  "Created",
  {"url":"https://api.github.com/repos/lihaoyi/test/issues/1", ...
