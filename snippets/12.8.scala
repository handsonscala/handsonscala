> val parsed = ujson.read(resp)
parsed: ujson.Value.Value = Arr(
  ArrayBuffer(
    Obj(
      Map(
        "url" -> Str("https://api.github.com/.../com-lihaoyi/upickle/issues/687"),
        "repository_url" -> Str("https://api.github.com/.../com-lihaoyi/upickle"),
...

> println(parsed.render(indent = 4))
[
    {
        "id": 3454385379,
        "number": 687,
        "title": "chore: Override size method in LinkedHashMap",
        "user": {
            "login": "<username elided>",
            "id": 501740,
...
