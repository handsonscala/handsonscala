enum Value:
  case Str(s: String)
  case Dict(pairs: Map[String, Value])
  case Func(call: Seq[Value] => Value)