> val b = Array.newBuilder[Int]
b: mutable.ArrayBuilder[Int] = ArrayBuilder.ofInt

> b += 1

> b += 2

> b.result()
res2: Array[Int] = Array(1, 2)
