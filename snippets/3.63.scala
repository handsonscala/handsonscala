@ var i = 0

@ val output = new Array[String](100)

@ flexibleFizzBuzz{s =>
    output(i) = s
    i += 1
  }

@ output
res125: Array[String] = Array(
  "1",
  "2",
  "Fizz",
  "4",
  "Buzz",
...
