val m = Map("one" -> 1, "two" -> 2, "three" -> 3)

assert(m.contains("two") == true)

assert(m("two") == 2)

val m2 = Map("one" -> 1, "two" -> 2, "three" -> 3)

assert(m2.get("one") == Some(1))

assert(m.get("four") == None)

assert(
  Vector(("one", 1), ("two", 2), ("three", 3)).to(Map) ==
  Map("one" -> 1, "two" -> 2, "three" -> 3)
)

assert(
  Map[String, Int]() + ("one" -> 1) + ("three" -> 3) ==
  Map("one" -> 1, "three" -> 3)
)

for ((k, v) <- m) println(k + " " + v)
