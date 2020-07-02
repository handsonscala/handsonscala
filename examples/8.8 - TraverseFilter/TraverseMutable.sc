val data = ujson.read(os.read.stream(os.pwd / "ammonite-releases.json"))

pprint.log(ujson.write(data, indent = 4), height = 20)

def traverse(v: ujson.Value): Boolean = v match{
  case a: ujson.Arr =>
    a.arr.foreach(traverse)
    true
  case o: ujson.Obj =>
    o.obj.filterInPlace{case (k, v) => traverse(v)}
    true
  case s: ujson.Str => !s.str.startsWith("https://")
  case _ => true
}

assert(data.toString.contains("https://"))

traverse(data)

assert(!data.toString.contains("https://"))
