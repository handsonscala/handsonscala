
trait StrWriter[T]{ def write(t: T): String }
object StrWriter:
  given WriteInt: StrWriter[Int]:
    def write(s: Int) = s.toString
  given WriteBoolean: StrWriter[Boolean]:
    def write(s: Boolean) = s.toString
  given WriteDouble: StrWriter[Double]:
    def write(s: Double) = s.toString
  given ParseSeq: [T] => (w: StrWriter[T]) => StrWriter[Seq[T]]:
    def write(t: Seq[T]) = t.map(w.write).mkString("[", ",", "]")

  given WriteTuple: [T, V] => (w1: StrWriter[T], w2: StrWriter[V]) => StrWriter[(T, V)]:
    def write(t: (T, V)) =
      val (left, right) = t
      "[" + w1.write(left) + "," + w2.write(right) + "]"

def writeToString[T](t: T)(using writer: StrWriter[T]): String = writer.write(t)

def writeToConsole[T](t: T)(using writer: StrWriter[T]): Unit =
  scala.Console.out.println(writer.write(t))
