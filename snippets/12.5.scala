> requests.post(
    "https://api.github.com/repos/lihaoyi/test/issues/1/comments",
    data = ujson.Obj("body" -> "world"),
    headers = Map("Authorization" -> s"token $token")
  )
res3: requests.Response = Response(
  url = "https://api.github.com/repos/lihaoyi/test/issues/1/comments",
  statusCode = 201,
  statusMessage = "Created",
  data = {"url":"https://.../repos/lihaoyi/test/issues/comments/573959489", ...
