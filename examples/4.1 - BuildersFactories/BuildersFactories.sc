val b = Array.newBuilder[Int]

b += 1

b += 2

assert(b.result().toSeq == Array(1, 2).toSeq)

assert( // Array with "hello" repeated 5 times
  Array.fill(5)("hello").toSeq ==
    Array("hello", "hello", "hello", "hello", "hello").toSeq
)
assert( // Array with 5 items, each computed from its index
  Array.tabulate(5)(n => s"hello $n").toSeq ==
    Array("hello 0", "hello 1", "hello 2", "hello 3", "hello 4").toSeq
)
assert( // Concatenating two Arrays into a larger one
  (Array(1, 2, 3) ++ Array(4, 5, 6)).toSeq ==
    Array(1, 2, 3, 4, 5, 6).toSeq
)
