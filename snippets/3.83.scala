> class PrettyPoint3D(x: Double, y: Double, z: Double) extends Point, Jsonable:
    def coordinates = Seq(x, y, z)
    override def toJson = "***" + super.toJson + "***"

> PrettyPoint3D(1, 2, 3).toJson
res0: String = "***[1.0, 2.0, 3.0]***"
