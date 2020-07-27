
trait StrWriter[T]{ def write(t: T): String }
object StrWriter{
  implicit object WriteInt extends StrWriter[Int]{
    def write(s: Int) = s.toString
  }
  implicit object WriteBoolean extends StrWriter[Boolean]{
    def write(s: Boolean) = s.toString
  }
  implicit object WriteDouble extends StrWriter[Double]{
    def write(s: Double) = s.toString
  }
  implicit def ParseSeq[T](implicit w: StrWriter[T]) = new StrWriter[Seq[T]]{
    def write(t: Seq[T]) = t.map(w.write).mkString("[", ",", "]")
  }

  implicit def WriteTuple[T, V](implicit w1: StrWriter[T], w2: StrWriter[V]) =
    new StrWriter[(T, V)]{
      def write(t: (T, V)) = {
        val (left, right) = t
        "[" + w1.write(left) + "," + w2.write(right) + "]"
      }
    }
}

def writeToString[T](t: T)(implicit writer: StrWriter[T]): String = writer.write(t)

def writeToConsole[T](t: T)(implicit writer: StrWriter[T]): Unit = {
  scala.Console.out.println(writer.write(t))
}
