> class FinalFoo(x: Int):
    final def printMsg(msg: String) = println(msg + x)

> class FinalFooOverride(x: Int) extends FinalFoo(x):
    override def printMsg(msg: String) = println(msg + x * 2)
-- [E164] Declaration Error: ---------------------------------------------------
  |error overriding method printMsg in class FinalFoo of type (msg: String): Unit;
  |  method printMsg of type (msg: String): Unit cannot override final member method
