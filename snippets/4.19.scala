@ val v = Vector(1, 2, 3, 4, 5)
v: Vector[Int] = Vector(1, 2, 3, 4, 5)

@ v(0)
res42: Int = 1

@ val v2 = v.updated(2, 10)
v2: Vector[Int] = Vector(1, 2, 10, 4, 5)

@ v2
res44: Vector[Int] = Vector(1, 2, 10, 4, 5)

@ v // note that `v` did not change!
res45: Vector[Int] = Vector(1, 2, 3, 4, 5)
