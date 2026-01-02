> Array(1, 2, 3, 4, 5, 6, 7).foldLeft(0)((x, y) => x + y) // overall sum
res19: Int = 28

> Array(1, 2, 3, 4, 5, 6, 7).foldLeft(1)((x, y) => x * y) // overall product
res20: Int = 5040

> Array(1, 2, 3, 4, 5, 6, 7).foldLeft(1)(_ * _) // same, but shorthand
res21: Int = 5040
