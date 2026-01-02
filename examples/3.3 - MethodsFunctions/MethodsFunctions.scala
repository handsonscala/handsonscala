// Methods

def printHello(times: Int) =
  println("hello " + times)

def printHello2(times: Int = 0) =
  println("hello " + times)

def hello(i: Int = 0) =
  "hello " + i

class Box(var x: Int):
  def update(f: Int => Int) = x = f(x)
  def printMsg(msg: String) =
    println(msg + x)

def increment(i: Int) = i + 1

def main() =
  printHello(1)

  printHello(times = 2) // argument name provided explicitly

  printHello2(1)

  printHello2()

  assert(hello(1) == "hello 1")

  println(hello())

  val helloHello = hello(123) + " " + hello(456)

  assert(helloHello.reverse == "654 olleh 321 olleh")

  // Function Values

  var g: Int => Int = i => i + 1

  assert(g(10) == 11)

  g = i => i * 2

  assert(g(10) == 20)

  val b = Box(1)

  b.printMsg("Hello")

  b.update(i => i + 5)

  b.printMsg("Hello")

  b.update(_ + 5)

  b.printMsg("Hello")

  val b2 = Box(123)

  b.update(increment) // Providing a method reference

  b.update(x => increment(x)) // Explicitly writing out the function literal

  b.update(increment(_)) // You can also use the `_` placeholder syntax

  b.printMsg("result: ")
