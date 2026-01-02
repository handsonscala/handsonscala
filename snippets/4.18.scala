> val v = Vector(1, 2, 3, 4)
v: Vector[Int] = Vector(1, 2, 3, 4)

> v(0)
res33: Int = 1

> val v2 = v.updated(2, 10)
v2: Vector[Int] = Vector(1, 2, 10, 4)

> println(v) // `v` is unchanged!
Vector(1, 2, 3, 4)
