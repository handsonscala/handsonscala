> trait Point:
    def magnitude: Double

> trait Jsonable:
    def toJson: String

> class Point2D(x: Double, y: Double) extends Point, Jsonable:
    def magnitude = math.sqrt(x * x + y * y)
    def toJson = s"[$x, $y]"

> class Point3D(x: Double, y: Double, z: Double) extends Point, Jsonable:
    def magnitude = math.sqrt(x * x + y * y + z * z)
    def toJson = s"[$x, $y, $z]"

> val points = Array(Point2D(1, 2), Point3D(4, 5, 6))

> for p <- points do println(p.magnitude)
2.23606797749979
8.774964387392123

> for p <- points do println(p.toJson)
[1, 2]
[4, 5, 6]
