> val a = mutable.ArrayDeque(1, 2, 3, 4)
a: mutable.ArrayDeque[Int] = ArrayDeque(1, 2, 3, 4)

> a.mapInPlace(_ + 1)
res62: mutable.ArrayDeque[Int] = ArrayDeque(2, 3, 4, 5)

> a.filterInPlace(_ % 2 == 0)
res63: mutable.ArrayDeque[Int] = ArrayDeque(2, 4)

> a // `a` was modified in place
res64: mutable.ArrayDeque[Int] = ArrayDeque(2, 4)
