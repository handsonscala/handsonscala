def main() =
  class Add(x: Int):
    def apply(y: Int) = x + y

  val add1 = Add(1)

  assert(add1(10) == 11)

  val addMore = Add(5)

  assert(addMore(10) == 15)

  class Foo(x: Int):
    def printMsg(msg: String) = println(msg + x)

  object Foo:
    def apply(s: String) = new Foo(s.toInt)

  Foo("123").printMsg("hello")
