import $ivy.`com.lihaoyi::castor:0.1.3`
import $file.StateMachine, StateMachine._

println("Sending...")
stateMachineUploader.send(Text("I am Cow"))
Thread.sleep(5000)

println("Sending...")
stateMachineUploader.send(Text("Hear me moo"))
println("Sending...")
stateMachineUploader.send(Text("I weigh twice as much as you"))
Thread.sleep(5000)

println("Sending...")
stateMachineUploader.send(Text("And I look good on the barbecue"))
cc.waitForInactivity()

// Validation
assert(stateMachineUploader.responseCount == 3)
