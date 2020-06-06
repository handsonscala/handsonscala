@ val parsed = ujson.read(resp.text())
parsed: ujson.Value.Value = Arr(
  ArrayBuffer(
    Obj(
      Map(
        "url" -> Str("https://api.github.com/repos/lihaoyi/upickle/issues/620"),
        "repository_url" -> Str("https://api.github.com/repos/lihaoyi/upickle"),

...
@ println(parsed.render(indent = 4))
[
    {
        "id": 449398451,
        "number": 620,
        "title": "Issue with custom repositories when trying to use Scoverage",
        "user": {
            "login": "jacarey",
            "id": 6933549,
...
