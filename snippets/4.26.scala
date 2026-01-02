> Vector(("one", 1), ("two", 2), ("three", 3)).to(Map)
res46: Map[String, Int] = Map("one" -> 1, "two" -> 2, "three" -> 3)

> Map[String, Int]() + ("one" -> 1) + ("three" -> 3)
res47: Map[String, Int] = Map("one" -> 1, "three" -> 3)

> for (k, v) <- m do println(k + " " + v)
one 1
two 2
three 3
