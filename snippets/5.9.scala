> enum Json:
    case Null()
    case Bool(value: Boolean)
    case Str(value: String)
    case Num(value: Double)
    case Arr(value: Seq[Json])
    case Dict(value: Map[String, Json])

> val myJsonDict = Json.Dict(Map("hello" -> Json.Str("world"), "number" -> Json.Num(1)))
myJsonDict: Json = Dict(Map("hello" -> Str("world"), "number" -> Num(1.0)))
