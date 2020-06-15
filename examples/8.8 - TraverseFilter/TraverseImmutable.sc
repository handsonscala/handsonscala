val data = ujson.read(os.read.stream(os.pwd / "ammonite-releases.json"))

pprint.log(ujson.write(data, indent = 4), height = 20)

def traverse(v: ujson.Value): ujson.Value = traverse0(v).get

def traverse0(v: ujson.Value): Option[ujson.Value] = v match{
  case a: ujson.Arr =>
    Some(ujson.Arr.from(a.arr.flatMap(traverse0)))
  case o: ujson.Obj =>
    Some(ujson.Obj.from(o.obj.flatMap{case (k, v) => traverse0(v).map(k -> _)}))
  case s: ujson.Str => if (!s.str.startsWith("https://")) Some(s) else None
  case _ => Some(v)
}

assert(data.toString.contains("https://"))

val newData = traverse(data)

assert(data.toString.contains("https://"))

assert(!newData.toString.contains("https://"))
