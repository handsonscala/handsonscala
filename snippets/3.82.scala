trait Point:
  def coordinates: Seq[Double]
  def magnitude: Double = math.sqrt(coordinates.map(v => v * v).sum)

trait Jsonable:
  def coordinates: Seq[Double]
  def toJson: String = "[" + coordinates.mkString(", ") + "]"

class Point2D(x: Double, y: Double) extends Point, Jsonable:
  def coordinates = Seq(x, y)

class Point3D(x: Double, y: Double, z: Double) extends Point, Jsonable:
  def coordinates = Seq(x, y, z)
