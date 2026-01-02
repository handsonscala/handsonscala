def main() =
  locally:
    enum Json:

      case Null()
      case Bool(value: Boolean)
      case Str(value: String)
      case Num(value: Double)
      case Arr(value: Seq[Json])
      case Dict(value: Map[String, Json])

    def stringify(value0: Json): String = value0 match
      case Json.Null() => "null"
      case Json.Bool(value) => value.toString
      case Json.Str(value) => "\"" + value.replace("\"", "\\\"") + "\""
      case Json.Num(value) => value.toString
      case Json.Arr(value) => "[" + value.map(stringify).mkString(", ") + "]"
      case Json.Dict(value) => "{" +  value.map{ (k, v) => stringify(Json.Str(k)) + ": " + stringify(v) } + "}"

    assert(stringify(Json.Str("hello")) == "\"hello\"")

    val dict = Json.Dict(
      Map(
        "hello" -> Json.Str("world"),
        "number" -> Json.Num(1)
      )
    )

    assert(stringify(dict) == """{List("hello": "world", "number": 1.0)}""")

  locally:
    enum Json:
      case Null()
      case Bool(value: Boolean)
      case Str(value: String)
      case Num(value: Double)
      case Arr(value: Seq[Json])
      case Dict(value: Map[String, Json])

      def stringify: String = this match
        case Json.Null() => "null"
        case Json.Bool(value) => value.toString
        case Json.Str(value) => "\"" + value.replace("\"", "\\\"") + "\""
        case Json.Num(value) => value.toString
        case Json.Arr(value) => "[" + value.map(_.stringify).mkString(", ") + "]"
        case Json.Dict(value) => "{" +  value.map{ (k, v) => Json.Str(k).stringify + ": " + v.stringify } + "}"

    assert(Json.Str("hello").stringify == "\"hello\"")

    val dict = Json.Dict(
      Map(
        "hello" -> Json.Str("world"),
        "number" -> Json.Num(1)
      )
    )

    assert(dict.stringify == """{List("hello": "world", "number": 1.0)}""")
