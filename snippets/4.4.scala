> Array(1, 2, 3, 4, 5).map(i => i * 2) // Multiply each element by 2
res5: Array[Int] = Array(2, 4, 6, 8, 10)

> Array(1, 2, 3, 4, 5).filter(i => i % 2 == 1) // only odd elements
res6: Array[Int] = Array(1, 3, 5)

> Array(1, 2, 3, 4, 5).take(2) // Keep first two elements
res7: Array[Int] = Array(1, 2)

> Array(1, 2, 3, 4, 5).drop(2) // Discard first two elements
res8: Array[Int] = Array(3, 4, 5)

> Array(1, 2, 3, 4, 5).slice(1, 4) // Keep elements from index 1-4
res9: Array[Int] = Array(2, 3, 4)

> Array(1, 2, 3, 4, 5, 4, 3, 2, 1, 2, 3, 4, 5).distinct // no duplicates
res10: Array[Int] = Array(1, 2, 3, 4, 5)
