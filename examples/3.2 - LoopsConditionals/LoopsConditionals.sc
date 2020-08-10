// For-Loops

var total = 0

val items = Array(1, 10, 100, 1000)

for (item <- items) total += item

assert(total == 1111)

var total2 = 0

for (i <- Range(0, 5)) {
  println("Looping " + i)
  total2 = total2 + i
}

assert(total2 == 10)

val multi = Array(Array(1, 2, 3), Array(4, 5, 6))

for (arr <- multi; i <- arr) println(i)

for (arr <- multi; i <- arr; if i % 2 == 0) println(i)

// If-Else

var total3 = 0

for (i <- Range(0, 10)) {
  if (i % 2 == 0) total3 += i
  else total3 += 2
}

assert(total3 == 30)

var total4 = 0

for (i <- Range(0, 10)) {
  total4 += (if (i % 2 == 0) i else 2)
}

assert(total4 == 30)

// FizzBuzz

for (i <- Range.inclusive(1, 100)) {
  if (i % 3 == 0 && i % 5 == 0) println("FizzBuzz")
  else if (i % 3 == 0) println("Fizz")
  else if (i % 5 == 0) println("Buzz")
  else println(i)
}

for (i <- Range.inclusive(1, 100)) {
  println(
    if (i % 3 == 0 && i % 5 == 0) "FizzBuzz"
    else if (i % 3 == 0) "Fizz"
    else if (i % 5 == 0) "Buzz"
    else i
  )
}

// Comprehensions

val a = Array(1, 2, 3, 4)

val a2 = for (i <- a) yield i * i
assert(a2.toSeq == Array(1, 4, 9, 16).toSeq)

val a3 = for (i <- a) yield "hello " + i
assert(a3.toSeq == Array("hello 1", "hello 2", "hello 3", "hello 4").toSeq)

val a4 = for (i <- a if i % 2 == 0) yield "hello " + i
assert(a4.toSeq == Array("hello 2", "hello 4").toSeq)

val a5 = Array(1, 2); val b5 = Array("hello", "world")
val flattened = for (i <- a5; s <- b5) yield s + i
assert(flattened.toSeq == Array("hello1", "world1", "hello2", "world2").toSeq)

val flattened2 = for{
  i <- a5
  s <- b5
} yield s + i
assert(flattened2.toSeq == Array("hello1", "world1", "hello2", "world2").toSeq)

val flattened3 = for{
  s <- b5
  i <- a5
} yield s + i

assert(flattened3.toSeq == Array("hello1", "hello2", "world1", "world2").toSeq)

val fizzbuzz = for (i <- Range.inclusive(1, 100)) yield {
  if (i % 3 == 0 && i % 5 == 0) "FizzBuzz"
  else if (i % 3 == 0) "Fizz"
  else if (i % 5 == 0) "Buzz"
  else i.toString
}

assert(fizzbuzz.take(5) == Vector("1", "2", "Fizz", "4", "Buzz"))
