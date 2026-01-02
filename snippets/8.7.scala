> data(0)
res1: ujson.Value = Obj(
  Map(
    "url" -> Str("https://api.github.com/repos/.../17991367"),
    "assets_url" -> Str("https://api.github.com/repos/.../17991367/assets"),
...

> data(0)("url")
res2: ujson.Value = Str(
  "https://api.github.com/repos/lihaoyi/Ammonite/releases/17991367"
)

> data(0)("author")("id")
res3: ujson.Value = Num(2.0607116E7)
