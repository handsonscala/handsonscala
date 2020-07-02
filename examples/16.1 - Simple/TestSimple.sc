import $ivy.`com.lihaoyi::castor:0.1.3`

import $file.Simple, Simple._

println("sending hello")
uploader.send("hello")

println("sending world")
uploader.send("world")

println("sending !")
uploader.send("!")

cc.waitForInactivity()

// Validation

assert(uploader.count == 3)
