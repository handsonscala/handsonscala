enum Json:
  case ...

  def stringify: String = this match
    case Json.Null() => "null"
    case Json.Bool(value) => value.toString
    case Json.Str(value) => "\"" + value.replace("\"", "\\\"") + "\""
    case Json.Num(value) => value.toString
    case Json.Arr(value) => "[" + value.map(_.stringify).mkString(", ") + "]"
    case Json.Dict(value) =>
      value.map{ (k, v) => Json.Str(k).stringify + ": " + v.stringify }.mkString("{", ", ", "}")

println(Json.Str("hello").stringify) // "hello"
