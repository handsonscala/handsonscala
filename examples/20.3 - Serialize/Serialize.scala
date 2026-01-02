//| moduleDeps: [Values.scala]
def serialize(v: Value): String = v.runtimeChecked match
  case Value.Str(s) => "\"" + s + "\""
  case Value.Dict(kvs) =>
    kvs.map((k, v) => "\"" + k + "\": " + serialize(v)).mkString("{", ", ", "}")
