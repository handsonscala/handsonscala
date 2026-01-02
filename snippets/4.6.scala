> Array(1, 2, 3, 4, 5, 6, 7).find(i => i % 2 == 0 && i > 4)
res13: Option[Int] = Some(6)

> Array(1, 2, 3, 4, 5, 6, 7).find(i => i % 2 == 0 && i > 10)
res14: Option[Int] = None

> Array(1, 2, 3, 4, 5, 6, 7).exists(x => x > 1) // any elements more than 1?
res15: Boolean = true

> Array(1, 2, 3, 4, 5, 6, 7).exists(_ < 0) // same as a.exists(x => x < 0)
res16: Boolean = false
