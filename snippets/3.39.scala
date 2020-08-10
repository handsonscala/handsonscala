@ val flattened = for{
    i <- a
    s <- b
  } yield s + i
flattened: Array[String] = Array("hello1", "world1", "hello2", "world2")

@ val flattened2 = for{
    s <- b
    i <- a
  } yield s + i
flattened2: Array[String] = Array("hello1", "hello2", "world1", "world2")
