
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
}

def parseFromString[T](s: String)(implicit parser: StrParser[T]) = parser.parse(s)

val args = Seq("123", "true", "7.5")
val myInt = parseFromString[Int](args(0))
val myBoolean = parseFromString[Boolean](args(1))
val myDouble = parseFromString[Double](args(2))

assert(myInt == 123)
assert(myBoolean == true)
assert(myDouble == 7.5)

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

implicit def ParseSeq[T](implicit p: StrParser[T]) = new StrParser[Seq[T]]{
  def parse(s: String) = splitExpressions(s).map(p.parse)
}

assert(parseFromString[Seq[Boolean]]("[true,false,true]") == Seq(true, false, true))
assert(parseFromString[Seq[Int]]("[1,2,3,4]") == Seq(1, 2, 3, 4))

implicit def ParseTuple[T, V](implicit p1: StrParser[T], p2: StrParser[V]) =
  new StrParser[(T, V)]{
    def parse(s: String) = {
      val Seq(left, right) = splitExpressions(s)
      (p1.parse(left), p2.parse(right))
    }
  }

assert(parseFromString[(Int, Boolean)]("[123,true]") == (123, true))
assert(parseFromString[(Boolean, Double)]("[true,1.5]") == (true, 1.5))

assert(
  parseFromString[Seq[(Int, Boolean)]]("[[1,true],[2,false],[3,true],[4,false]]") ==
  Seq((1, true), (2, false), (3, true), (4, false))
)

assert(
  parseFromString[(Seq[Int], Seq[Boolean])]("[[1,2,3,4,5],[true,false,true]]") ==
  (Seq(1, 2, 3, 4, 5), Seq(true, false, true))
)

val nested = parseFromString[Seq[(Seq[Int], Seq[Boolean])]](
  "[[[1],[true]],[[2,3],[false,true]],[[4,5,6],[false,true,false]]]"
)
pprint.log(nested)
assert(
  nested ==
  Seq(
    (Seq(1), Seq(true)),
    (Seq(2, 3), Seq(false, true)),
    (Seq(4, 5, 6), Seq(false, true, false))
  )
)

val nested2 = parseFromString[Seq[(Seq[Int], Seq[(Boolean, Double)])]](
  "[[[1],[[true,0.5]]],[[2,3],[[false,1.5],[true,2.5]]]]"
)

pprint.log(nested2)

assert(
  nested2 ==
  Seq(
    (Seq(1), Seq((true, 0.5))),
    (Seq(2, 3), Seq((false, 1.5), (true, 2.5)))
  )
)
