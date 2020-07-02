@ val v = Vector[Int]()
v: Vector[Int] = Vector()

@ val v1 = v :+ 1
v1: Vector[Int] = Vector(1)

@ val v2 = 4 +: v1
v2: Vector[Int] = Vector(4, 1)

@ val v3 = v2.tail
v3: Vector[Int] = Vector(1)
