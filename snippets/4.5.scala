> val a = Array(1, 2, 3, 4, 5)
a: Array[Int] = Array(1, 2, 3, 4, 5)

> val a2 = a.map(x => x + 10)
a2: Array[Int] = Array(11, 12, 13, 14, 15)

> a(0) // Note that `a` is unchanged!
res11: Int = 1

> a2(0) // Only `a2` contains the new value, since it was the one that was modified
res12: Int = 11
