> val numbers = upickle.read[Seq[Int]]("[1, 2, 3, 4]")
numbers: Seq[Int] = List(1, 2, 3, 4)

> upickle.write(numbers)
res10: String = "[1,2,3,4]"

> val tuples = upickle.read[Seq[(Int, Boolean)]](
    "[[1, true], [2, false]]"
  )
tuples: Seq[(Int, Boolean)] = List((1, true), (2, false))

> upickle.write(tuples)
res11: String = "[[1,true],[2,false]]"
