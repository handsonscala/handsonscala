def main() =
  object Thing:
    var x = 1
    def hello = "world " + x

  assert(Thing.hello == "world 1")

  Thing.x = 5

  assert(Thing.hello == "world 5")

  class Box(var x: Int):
    def update(f: Int => Int) =
      x = f(x)
    def printMsg(msg: String) =
      println(msg + x)

  object SingletonBox extends Box(10)

  SingletonBox.update(x => x + 5)

  SingletonBox.printMsg("hello")
