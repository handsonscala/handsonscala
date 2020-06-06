@ trait Point{ def hypotenuse: Double }

@ class Point2D(x: Double, y: Double) extends Point{
    def hypotenuse = math.sqrt(x * x + y * y)
  }

@ class Point3D(x: Double, y: Double, z: Double) extends Point{
    def hypotenuse = math.sqrt(x * x + y * y + z * z)
  }

@ val points: Array[Point] = Array(new Point2D(1, 2), new Point3D(4, 5, 6))

@ for (p <- points) println(p.hypotenuse)
2.23606797749979
8.774964387392123
