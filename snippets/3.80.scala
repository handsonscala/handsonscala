> abstract class AbstractFoo(x: Int):
    def printMsg(msg: String): Unit

> class ConcreteFoo1(x: Int) extends AbstractFoo(x) // Missing abstract method
-- Error: ----------------------------------------------------------------------
  |class ConcreteFoo1 needs to be abstract, since def printMsg(msg: String): Unit
  |in class AbstractFoo is not defined

> class ConcreteFoo1(x: Int) extends AbstractFoo(x): // OK
    def printMsg(msg: String) = println(msg + x) 
