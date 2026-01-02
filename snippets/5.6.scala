> {
  sealed trait Point
  case class Point2D(x: Double, y: Double) extends Point
  case class Point3D(x: Double, y: Double, z: Double) extends Point
  }

> def magnitude(p: Point) = p match
    case Point2D(x, y) => math.sqrt(x * x + y * y)
    case Point3D(x, y, z) => math.sqrt(x * x + y * y + z * z)

> val points: Array[Point] = Array(Point2D(1, 2), Point3D(4, 5, 6))
points: Array[Point] = Array(
  Point2D(x = 1.0, y = 2.0),
  Point3D(x = 4.0, y = 5.0, z = 6.0)
)

> for p <- points do println(magnitude(p))
2.23606797749979
8.774964387392123
