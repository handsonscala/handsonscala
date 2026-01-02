class Box(var x: Int):
  def update(f: Int => Int) =
    x = f(x)

  def printMsg(msg: String) =
    val finalMsg = msg + x
    println(msg + x)

val myBox = new Box(10)

myBox.update: previous =>
  println(s"Incrementing $previous!")
  previous + 1

myBox.printMsg("hello") // hello11
