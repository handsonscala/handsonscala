assert( // Multiply every element by 2
  Array(1, 2, 3, 4, 5).map(i => i * 2).toSeq ==
  Array(2, 4, 6, 8, 10).toSeq
)

assert( // Keep only elements not divisible by 2
  Array(1, 2, 3, 4, 5).filter(i => i % 2 == 1).toSeq ==
  Array(1, 3, 5).toSeq
)

assert( // Keep first two elements
  Array(1, 2, 3, 4, 5).take(2).toSeq ==
  Array(1, 2).toSeq
)

assert( // Discard first two elements
  Array(1, 2, 3, 4, 5).drop(2).toSeq ==
  Array(3, 4, 5).toSeq
)

assert( // Keep elements from index 1-4
  Array(1, 2, 3, 4, 5).slice(1, 4) .toSeq ==
  Array(2, 3, 4).toSeq
)

assert( //  Removes all deuplicates
  Array(1, 2, 3, 4, 5, 4, 3, 2, 1, 2, 3, 4, 5, 6, 7, 8).distinct.toSeq ==
  Array(1, 2, 3, 4, 5, 6, 7, 8).toSeq
)

val a = Array(1, 2, 3, 4, 5)

val a2 = a.map(x => x + 10)

assert(a(0) == 1) // Note that `a` is unchanged!

assert(a2(0) == 11)
