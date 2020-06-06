@ Array(1, 2, 3, 4, 5, 6, 7).find(i => i % 2 == 0 && i > 4)
res17: Option[Int] = Some(6)

@ Array(1, 2, 3, 4, 5, 6, 7).find(i => i % 2 == 0 && i > 10)
res18: Option[Int] = None

@ Array(1, 2, 3, 4, 5, 6, 7).exists(x => x > 1) // are any elements greater than 1?
res19: Boolean = true

@ Array(1, 2, 3, 4, 5, 6, 7).exists(_ < 0) // same as a.exists(x => x < 0)
res20: Boolean = false
