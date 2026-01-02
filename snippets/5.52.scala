trait StrParser[T]:
  def parse(s: String): T

object StrParser:
  given ParseInt: StrParser[Int]:
    def parse(s: String) = s.toInt

  given ParseBoolean: StrParser[Boolean]:
    def parse(s: String) = s.toBoolean

  given ParseDouble: StrParser[Double]:
    def parse(s: String) = s.toDouble
