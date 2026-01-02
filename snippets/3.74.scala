> class Qux(var x: Int):
    def printMsg(msg: String) =
      // `x` is a `var` so we can modify it
      x += 1
      println(msg + x)
