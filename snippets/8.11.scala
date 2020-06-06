@ def traverse(v: ujson.Value): Iterable[String] = v match {
    case a: ujson.Arr => a.arr.map(traverse).flatten
    case o: ujson.Obj => o.obj.values.map(traverse).flatten
    case s: ujson.Str => Seq(s.str)
    case _ => Nil
  }

@ traverse(data)
res21: Iterable[String] = ArrayBuffer(
  "https://api.github.com/repos/.../releases/17991367",
  "https://api.github.com/repos/.../releases/17991367/assets",
  "https://uploads.github.com/repos/.../releases/17991367/assets",
  "https://github.com/.../releases/tag/1.6.8",
...
