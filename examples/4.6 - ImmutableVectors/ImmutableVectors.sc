val v = Vector(1, 2, 3, 4, 5)

assert(v(0) == 1)

val v2 = v.updated(2, 10)

assert(v2 == Vector(1, 2, 10, 4, 5))

assert(v == Vector(1, 2, 3, 4, 5)) // note that `v` did not change!

val v0 = Vector[Int]()

val v01 = v0 :+ 1

assert(v01 == Vector(1))

val v02 = 4 +: v01

assert(v02 == Vector(4, 1))

val v03 = v02.tail

assert(v03 == Vector(1))
