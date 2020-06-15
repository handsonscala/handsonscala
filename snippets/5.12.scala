@ for (i <- Range.inclusive(1, 100)) {
    val s =  (i % 3, i % 5) match {
      case (0, 0) => "FizzBuzz"
      case (0, _) => "Fizz"
      case (_, 0) => "Buzz"
      case _ => i
    }
    println(s)
  }
1
2
Fizz
4
Buzz
...
