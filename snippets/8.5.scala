sealed trait Value

case class Str(value: String) extends Value
case class Obj(value: mutable.LinkedHashMap[String, Value]) extends Value
case class Arr(value: mutable.ArrayBuffer[Value]) extends Value
case class Num(value: Double) extends Value

sealed trait Bool extends Value
case object False extends Bool
case object True extends Bool
case object Null extends Value
