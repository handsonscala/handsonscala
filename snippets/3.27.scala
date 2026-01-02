> val arr = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

> arr.filter(_ % 2 == 0)
res1: Array[Int] = Array(2, 4, 6, 8, 10)

> arr.filter(_ % 2 == 0).map(_ * 10)
res2: Array[Int] = Array(20, 40, 60, 80, 100)

> arr.filter(_ % 2 == 0).map(_ * 10).mkString(" ")
res3: String = "20 40 60 80 100"

> arr.filter(_ % 2 == 0).map(_ * 10).sum
res4: Int = 300

> arr.filter(_ % 2 == 0).map(_ * 10).reduce(_ * _)
res5: Int = 384000000
