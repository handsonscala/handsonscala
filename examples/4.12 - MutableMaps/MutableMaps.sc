val m1 = collection.mutable.Map("one" -> 1, "two" -> 2, "three" -> 3)

assert(m1.remove("two") == Some(2))

m1("five") = 5

assert(m1 == collection.mutable.Map("five" -> 5, "three" -> 3, "one" -> 1))

val m2 = collection.mutable.Map("one" -> 1, "two" -> 2, "three" -> 3)

assert(m2.getOrElseUpdate("three", -1) == 3)// already present, returns existing value

assert(m2 == collection.mutable.Map("one" -> 1, "two" -> 2, "three" -> 3))

assert(m2.getOrElseUpdate("four", -1) == -1) // not present, stores new value in map and returns it

assert(m2 == Map("two" -> 2, "three" -> 3, "four" -> -1,  "one" -> 1)) // `m2` now contains "four" -> -1
