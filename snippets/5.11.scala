object Json:
  def stringify(value0: Json): String = ...

println(Json.stringify(Json.Str("hello"))) // "hello"
