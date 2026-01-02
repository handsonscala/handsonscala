class Foo(x: Int):
  def printMsg(msg: String) =
    println(msg + x)

class Bar(val x: Int):
  def printMsg(msg: String) =
    println(msg + x)

class Qux(var x: Int):
  def printMsg(msg: String) =
    x += 1
    println(msg + x)

class Baz(x: Int):
  val bangs = "!" * x
  def printMsg(msg: String) =
    println(msg + bangs)

// Traits

trait Point{ def magnitude: Double }

class Point2D(x: Double, y: Double) extends Point:
  def magnitude = math.sqrt(x * x + y * y)

class Point3D(x: Double, y: Double, z: Double) extends Point:
  def magnitude = math.sqrt(x * x + y * y + z * z)

def main() =
  val f = Foo(1)

  f.printMsg("hello")

  val b = Bar(1)

  assert(b.x == 1)

  val q = Qux(1)

  q.printMsg("hello")

  q.printMsg("hello")

  val z = Baz(3)

  z.printMsg("hello")

  val points: Array[Point] = Array(Point2D(1, 2), Point3D(4, 5, 6))

  val results = for p <- points yield p.magnitude

  assert(results.toSeq == Seq(2.23606797749979, 8.774964387392123))
