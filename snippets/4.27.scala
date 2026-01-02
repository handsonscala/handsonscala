> Array(1, 2, 3).to(Vector)
res30: Vector[Int] = Vector(1, 2, 3)

> Array(1, 1, 2, 2, 3, 4).to(Set)
res31: Set[Int] = Set(1, 2, 3, 4)

> Vector(1, 2, 3).to(mutable.ArrayDeque)
res32: mutable.ArrayDeque[Int] = ArrayDeque(1, 2, 3)

> Map(1 -> "one", 2 -> "two").to(mutable.Buffer)
res33: mutable.Buffer[(Int, String)] = ArrayBuffer((1, "one"), (2, "two"))
