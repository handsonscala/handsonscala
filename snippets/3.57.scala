@ class Qux(var x: Int) {
    def printMsg(msg: String) = {
      x += 1
      println(msg + x)
    }
  }
