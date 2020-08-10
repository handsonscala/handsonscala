class Foo(x: Int) {
  def printMsg(msg: String) = {
    println(msg + x)
  }
}

val f = new Foo(1)

f.printMsg("hello")

class Bar(val x: Int) {
  def printMsg(msg: String) = {
    println(msg + x)
  }
}

val b = new Bar(1)

assert(b.x == 1)

class Qux(var x: Int) {
  def printMsg(msg: String) = {
    x += 1
    println(msg + x)
  }
}

val q = new Qux(1)

q.printMsg("hello")

q.printMsg("hello")

class Baz(x: Int) {
  val bangs = "!" * x
  def printMsg(msg: String) = {
    println(msg + bangs)
  }
}

val z = new Baz(3)

z.printMsg("hello")

// Traits

trait Point{ def hypotenuse: Double }

class Point2D(x: Double, y: Double) extends Point{
  def hypotenuse = math.sqrt(x * x + y * y)
}

class Point3D(x: Double, y: Double, z: Double) extends Point{
  def hypotenuse = math.sqrt(x * x + y * y + z * z)
}

val points: Array[Point] = Array(new Point2D(1, 2), new Point3D(4, 5, 6))

val results = for (p <- points) yield p.hypotenuse

assert(results.toSeq == Seq(2.23606797749979, 8.774964387392123))
