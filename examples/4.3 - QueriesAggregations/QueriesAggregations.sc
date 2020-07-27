assert(Array(1, 2, 3, 4, 5, 6, 7).find(i => i % 2 == 0 && i > 4) == Some(6))

assert(Array(1, 2, 3, 4, 5, 6, 7).find(i => i % 2 == 0 && i > 10) == None)

assert(Array(1, 2, 3, 4, 5, 6, 7).exists(x => x > 1) == true) // are any elements greater than 1?

assert(Array(1, 2, 3, 4, 5, 6, 7).exists(_ < 0) == false) // same as a.exists(x => x < 0)

assert(Array(1, 2, 3, 4, 5, 6, 7).mkString(",") == "1,2,3,4,5,6,7")

assert(Array(1, 2, 3, 4, 5, 6, 7).mkString("[", ",", "]") == "[1,2,3,4,5,6,7]")

assert(Array(1, 2, 3, 4, 5, 6, 7).foldLeft(0)((x, y) => x + y) == 28) // sum of all elements

assert(Array(1, 2, 3, 4, 5, 6, 7).foldLeft(1)((x, y) => x * y) == 5040) // product of all elements

assert(Array(1, 2, 3, 4, 5, 6, 7).foldLeft(1)(_ * _) == 5040) // same as above, shorthand syntax

val total = {
  var total = 0
  for (i <- Array(1, 2, 3, 4, 5, 6, 7)) total += i
  total
}

assert(total == 28)

val grouped = Array(1, 2, 3, 4, 5, 6, 7).groupBy(_ % 2)

assert(grouped(0).toSeq == Array(2, 4, 6).toSeq)

assert(grouped(1).toSeq == Array(1, 3, 5, 7).toSeq)
