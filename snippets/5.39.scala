trait StrParser[T]{ def parse(s: String): T }
object ParseInt extends StrParser[Int]{ def parse(s: String) = s.toInt }
object ParseBoolean extends StrParser[Boolean]{ def parse(s: String) = s.toBoolean }
object ParseDouble extends StrParser[Double]{ def parse(s: String) = s.toDouble }
