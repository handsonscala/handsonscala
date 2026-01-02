class Foo(x: Int):
  def printMsg(msg: String) = println(msg + x)

object Foo:
  def apply(s: String): Foo = new Foo(s.toInt)
