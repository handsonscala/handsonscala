> def stringify(value0: Json): String = value0 match
    case Json.Null() => "null"
    case Json.Bool(value) => value.toString
    case Json.Str(value) => "\"" + value.replace("\"", "\\\"") + "\""
    case Json.Num(value) => value.toString
    case Json.Arr(value) => "[" + value.map(stringify).mkString(", ") + "]"
    case Json.Dict(value) =>
      value.map((k, v) => stringify(Json.Str(k)) + ": " + stringify(v)).mkString("{", ", ", "}")

> stringify(myJsonDict)
"{\"hello\": \"world\", \"number\": 1.0}"
