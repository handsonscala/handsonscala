
trait StrParser[T]{ def parse(s: String): T }
object StrParser:
  given ParseInt: StrParser[Int]:
    def parse(s: String) = s.toInt
  given ParseBoolean: StrParser[Boolean]:
    def parse(s: String) = s.toBoolean
  given ParseDouble: StrParser[Double]:
    def parse(s: String) = s.toDouble

  given ParseSeq: [T] => (p: StrParser[T]) => StrParser[Seq[T]]:
    def parse(s: String) = s.split(',').toSeq.map(p.parse)

  given ParseTuple: [T, V] => (p1: StrParser[T], p2: StrParser[V]) => StrParser[(T, V)]:
    def parse(s: String) =
      val Array(left, right) = s.split('=')
      (p1.parse(left), p2.parse(right))

def parseFromString[T](s: String)(using parser: StrParser[T]) =
  parser.parse(s)

def parseFromConsole[T](using parser: StrParser[T]) =
  parser.parse(scala.Console.in.readLine())
