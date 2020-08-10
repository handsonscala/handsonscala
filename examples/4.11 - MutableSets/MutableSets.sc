
val s = collection.mutable.Set(1, 2, 3)

assert(s.contains(2) == true)

assert(s.contains(4) == false)

s.add(4)

s.remove(1)

assert(s == collection.mutable.Set(2, 3, 4))
