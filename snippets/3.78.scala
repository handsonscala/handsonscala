> class FooPrintsTwice(x: Int) extends Foo(x * 2):
    override def printMsg(msg: String) =
      super.printMsg(msg)
      super.printMsg(msg * 2)

> new FooPrintsTwice(123).printMsg("hello")
hello246
hello246hello246
