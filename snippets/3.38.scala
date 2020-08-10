@ val a = Array(1, 2); val b = Array("hello", "world")

@ val flattened = for (i <- a; s <- b) yield s + i
flattened: Array[String] = Array("hello1", "world1", "hello2", "world2")
