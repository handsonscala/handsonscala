val a = collection.mutable.ArrayDeque(1, 2, 3, 4)

a.mapInPlace(_ + 1)

a.filterInPlace(_ % 2 == 0)

assert(a == collection.mutable.ArrayDeque(2, 4))
