val s = Set(1, 2, 3)

assert(s.contains(2) == true)

assert(s.contains(4) == false)

assert(Set(1, 2, 3) + 4 + 5 == Set(5, 1, 2, 3, 4))

assert(Set(1, 2, 3) - 2 == Set(1, 3))

assert(Set(1, 2, 3) ++ Set(2, 3, 4) == Set(1, 2, 3, 4))

for (i <- Set(1, 2, 3, 4, 5)) println(i)
