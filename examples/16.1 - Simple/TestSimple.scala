//| moduleDeps: [Simple.scala]
def main() =
  println("sending hello")
  uploader.send("hello")

  println("sending world")
  uploader.send("world")

  println("sending !")
  uploader.send("!")

  cc.waitForInactivity()

  // Validation

  assert(uploader.count == 3)
