@ requests.post(
    "https://api.github.com/repos/lihaoyi/test/issues/1/comments",
    data = ujson.Obj("body" -> "world"),
    headers = Map("Authorization" -> s"token $token")
  )
res2: requests.Response = Response(
  "https://api.github.com/repos/lihaoyi/test/issues/1/comments",
  201,
  "Created",
  {"url":"https://api.github.com/repos/lihaoyi/test/issues/comments/573959489", ...
