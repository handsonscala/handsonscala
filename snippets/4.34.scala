> val m = mutable.Map("one" -> 1, "two" -> 2, "three" -> 3)

> m.getOrElseUpdate("three", -1) // already present, returns existing value
res59: Int = 3

> m // `m` is unchanged
res60: mutable.Map[String, Int] = HashMap(
  "two" -> 2,
  "three" -> 3,
  "one" -> 1
)

> m.getOrElseUpdate("four", -1) // not present, put and returns new value
res63: Int = -1

> m // `m` now contains "four" -> -1
res61: mutable.Map[String, Int] = HashMap(
  "two" -> 2,
  "three" -> 3,
  "four" -> -1,
  "one" -> 1
)
