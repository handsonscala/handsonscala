def main() =
  // For-Loops

  var total = 0

  val items = Array(1, 10, 100, 1000)

  for item <- items do total += item

  assert(total == 1111)

  var total2 = 0

  for i <- Range(0, 5) do
    println("Looping " + i)
    total2 = total2 + i

  assert(total2 == 10)

  val multi = Array(Array(1, 2, 3), Array(4, 5, 6))

  for arr <- multi; i <- arr do println(i)

  for arr <- multi; i <- arr; if i % 2 == 0 do println(i)

  // If-Else

  var total3 = 0

  for i <- Range(0, 10) do
    if i % 2 == 0 then total3 += i
    else total3 += 2

  assert(total3 == 30)

  var total4 = 0

  for i <- Range(0, 10) do
    total4 += (if i % 2 == 0 then i else 2)

  assert(total4 == 30)

  // FizzBuzz

  for i <- Range.inclusive(1, 100) do
    if i % 3 == 0 && i % 5 == 0 then println("FizzBuzz")
    else if i % 3 == 0 then println("Fizz")
    else if i % 5 == 0 then println("Buzz")
    else println(i)

  for i <- Range.inclusive(1, 100) do
    println(
      if i % 3 == 0 && i % 5 == 0 then "FizzBuzz"
      else if i % 3 == 0 then "Fizz"
      else if i % 5 == 0 then "Buzz"
      else i
    )

  // Comprehensions

  val a = Array(1, 2, 3, 4)

  val a2 = for i <- a yield i * i
  assert(a2.toSeq == Array(1, 4, 9, 16).toSeq)

  val a3 = for i <- a yield "hello " + i
  assert(a3.toSeq == Array("hello 1", "hello 2", "hello 3", "hello 4").toSeq)

  val a4 = for i <- a if i % 2 == 0 yield "hello " + i
  assert(a4.toSeq == Array("hello 2", "hello 4").toSeq)

  val a5 = Array(1, 2); val b5 = Array("hello", "world")
  val flattened = for i <- a5; s <- b5 yield s + i
  assert(flattened.toSeq == Array("hello1", "world1", "hello2", "world2").toSeq)

  val flattened2 = for
    i <- a5
    s <- b5
  yield s + i
  assert(flattened2.toSeq == Array("hello1", "world1", "hello2", "world2").toSeq)

  val flattened3 = for
    s <- b5
    i <- a5
  yield s + i

  assert(flattened3.toSeq == Array("hello1", "hello2", "world1", "world2").toSeq)

  val fizzbuzz = for i <- Range.inclusive(1, 100) yield
    if i % 3 == 0 && i % 5 == 0 then "FizzBuzz"
    else if i % 3 == 0 then "Fizz"
    else if i % 5 == 0 then "Buzz"
    else i.toString

  assert(fizzbuzz.take(5) == Vector("1", "2", "Fizz", "4", "Buzz"))
