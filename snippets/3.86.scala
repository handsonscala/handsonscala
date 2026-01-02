> class Box(var x: Int):
    def update(f: Int => Int) =
      x = f(x)
    def printMsg(msg: String) =
      println(msg + x)

> object SingletonBox extends Box(10)
