//| moduleDeps: [Batch.scala]
def main() =
  println("sending hello")
  batchUploader.send("hello")

  println("sending world")
  batchUploader.send("world")

  println("sending !")
  batchUploader.send("!")

  cc.waitForInactivity()

  // Validation

  pprint.log(batchUploader.responseCount)
  assert(batchUploader.responseCount == 1)
