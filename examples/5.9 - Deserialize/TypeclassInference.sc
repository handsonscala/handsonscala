
trait StrParser[T]{ def parse(s: String): T }
object StrParser{
  implicit object ParseInt extends StrParser[Int]{
    def parse(s: String) = s.toInt
  }
  implicit object ParseBoolean extends StrParser[Boolean]{
    def parse(s: String) = s.toBoolean
  }
  implicit object ParseDouble extends StrParser[Double]{
    def parse(s: String) = s.toDouble
  }

  implicit def ParseSeq[T](implicit p: StrParser[T]) = new StrParser[Seq[T]]{
    def parse(s: String) = splitExpressions(s).map(p.parse)
  }

  implicit def ParseTuple[T, V](implicit p1: StrParser[T], p2: StrParser[V]) =
    new StrParser[(T, V)]{
      def parse(s: String) = {
        val Seq(left, right) = splitExpressions(s)
        (p1.parse(left), p2.parse(right))
      }
    }
}

def parseFromString[T](s: String)(implicit parser: StrParser[T]) = parser.parse(s)

def parseFromConsole[T](implicit parser: StrParser[T]) = {
  parser.parse(scala.Console.in.readLine())
}

def splitExpressions(s: String): Seq[String] = {
  assert(s.head == '[')
  assert(s.last == ']')
  val indices = collection.mutable.ArrayDeque.empty[Int]
  var openBrackets = 0
  for(i <- Range(1, s.length - 1)){
    s(i) match{
      case '[' => openBrackets += 1
      case ']' => openBrackets -= 1
      case ',' =>
        if (openBrackets == 0) indices += i
      case _ => // do nothing
    }
  }
  val allIndices = Seq(0) ++ indices ++ Seq(s.length - 1)
  for(i <- Range(1, allIndices.length).toList)
  yield s.substring(allIndices(i - 1) + 1, allIndices(i))
}
