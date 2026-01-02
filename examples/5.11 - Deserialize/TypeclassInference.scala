
trait StrParser[T]{ def parse(s: String): T }
object StrParser:
  given ParseInt: StrParser[Int]:
    def parse(s: String) = s.toInt
  given ParseBoolean: StrParser[Boolean]:
    def parse(s: String) = s.toBoolean
  given ParseDouble: StrParser[Double]:
    def parse(s: String) = s.toDouble

  given ParseSeq: [T] => (p: StrParser[T]) => StrParser[Seq[T]]:
    def parse(s: String) = splitExpressions(s).map(p.parse)

  given ParseTuple: [T, V] => (p1: StrParser[T], p2: StrParser[V]) => StrParser[(T, V)]:
    def parse(s: String) =
      val Seq(left, right) = splitExpressions(s)
      (p1.parse(left), p2.parse(right))

def parseFromString[T](s: String)(using parser: StrParser[T]) = parser.parse(s)

def parseFromConsole[T](using parser: StrParser[T]) =
  parser.parse(scala.Console.in.readLine())

def splitExpressions(s: String): Seq[String] =
  assert(s.head == '[')
  assert(s.last == ']')
  val indices = collection.mutable.ArrayDeque.empty[Int]
  var openBrackets = 0
  for i <- Range(1, s.length - 1) do
    s(i) match
      case '[' => openBrackets += 1
      case ']' => openBrackets -= 1
      case ',' =>
        if openBrackets == 0 then indices += i
      case _ => // do nothing
  val allIndices = Seq(0) ++ indices ++ Seq(s.length - 1)
  for i <- Range(1, allIndices.length).toList
  yield s.substring(allIndices(i - 1) + 1, allIndices(i))
