@ val grouped = Array(1, 2, 3, 4, 5, 6, 7).groupBy(_ % 2)
grouped: Map[Int, Array[Int]] = Map(0 -> Array(2, 4, 6), 1 -> Array(1, 3, 5, 7))

@ grouped(0)
res26: Array[Int] = Array(2, 4, 6)

@ grouped(1)
res27: Array[Int] = Array(1, 3, 5, 7)
