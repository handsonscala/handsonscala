> Array.fill(5)("hello") // Array with "hello" repeated 5 times
res3: Array[String] = Array("hello", "hello", "hello", "hello", "hello")

> Array.tabulate(5)(n => s"hey! $n") // each value computed from the index
res4: Array[String] = Array(
  "hey! 0",
  "hey! 1",
  "hey! 2",
  "hey! 3",
  "hey! 4"
)

> Array(1, 2, 3) ++ Array(4, 5, 6) // Concat two Arrays into a larger one
res5: Array[Int] = Array(1, 2, 3, 4, 5, 6)
