@ val numbers = upickle.default.read[Seq[Int]]("[1, 2, 3, 4]")
numbers: Seq[Int] = List(1, 2, 3, 4)

@ upickle.default.write(numbers)
res23: String = "[1,2,3,4]"

@ val tuples = upickle.default.read[Seq[(Int, Boolean)]]("[[1, true], [2, false]]")
tuples: Seq[(Int, Boolean)] = List((1, true), (2, false))

@ upickle.default.write(tuples)
res25: String = "[[1,true],[2,false]]"
