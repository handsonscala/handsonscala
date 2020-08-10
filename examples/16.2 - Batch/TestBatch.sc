import $ivy.`com.lihaoyi::castor:0.1.7`

import $file.Batch, Batch._

println("sending hello")
batchUploader.send("hello")

println("sending world")
batchUploader.send("world")

println("sending !")
batchUploader.send("!")

cc.waitForInactivity()

// Validation

assert(batchUploader.responseCount == 1)
