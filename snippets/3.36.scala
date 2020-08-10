@ val a = Array(1, 2, 3, 4)

@ val a2 = for (i <- a) yield i * i
a2: Array[Int] = Array(1, 4, 9, 16)

@ val a3 = for (i <- a) yield "hello " + i
a3: Array[String] = Array("hello 1", "hello 2", "hello 3", "hello 4")
