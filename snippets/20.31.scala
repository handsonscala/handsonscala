sealed trait Value
object Value{
  case class Str(s: String) extends Value
  case class Dict(pairs: Map[String, Value]) extends Value
  case class Func(call: Seq[Value] => Value) extends Value
}