def flexibleFizzBuzz(handleLine: String => Unit): Unit = {
  for (i <- Range.inclusive(1, 100)) {
    handleLine(
      if (i % 3 == 0 && i % 5 == 0) "FizzBuzz"
      else if (i % 3 == 0) "Fizz"
      else if (i % 5 == 0) "Buzz"
      else i.toString
    )
  }
}

flexibleFizzBuzz(s => println(s))

var i = 0
val output = new Array[String](100)

flexibleFizzBuzz{s =>
  output(i) = s
  i += 1
}

assert(
  output.take(15).sameElements(
    Array(
      "1",
      "2",
      "Fizz",
      "4",
      "Buzz",
      "Fizz",
      "7",
      "8",
      "Fizz",
      "Buzz",
      "11",
      "Fizz",
      "13",
      "14",
      "FizzBuzz"
    )
  )
)
