val data = ujson.read(os.read.stream(os.pwd / "ammonite-releases.json"))

pprint.log(ujson.write(data, indent = 4), height = 20)

def traverse(v: ujson.Value): Iterable[String] = v match {
  case a: ujson.Arr => a.arr.flatMap(traverse)
  case o: ujson.Obj => o.obj.values.flatMap(traverse)
  case s: ujson.Str => Seq(s.str)
  case _ => Nil
}

assert(
  pprint.log(traverse(data).take(10), height = 20) ==
  Seq(
    "https://api.github.com/repos/lihaoyi/Ammonite/releases/17991367",
    "https://api.github.com/repos/lihaoyi/Ammonite/releases/17991367/assets",
    "https://uploads.github.com/repos/lihaoyi/Ammonite/releases/17991367/assets{?name,label}",
    "https://github.com/lihaoyi/Ammonite/releases/tag/1.6.8",
    "MDc6UmVsZWFzZTE3OTkxMzY3",
    "1.6.8",
    "master",
    "1.6.8",
    "Ammonite-Bot",
    "MDQ6VXNlcjIwNjA3MTE2"
  )
)
