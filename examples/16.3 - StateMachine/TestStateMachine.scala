//| moduleDeps: [StateMachine.scala]
def main() =
  println("Sending...")
  stateMachineUploader.send(Msg.Text("I am Cow"))
  Thread.sleep(5000)

  println("Sending...")
  stateMachineUploader.send(Msg.Text("Hear me moo"))
  println("Sending...")
  stateMachineUploader.send(Msg.Text("I weigh twice as much as you"))
  Thread.sleep(5000)

  println("Sending...")
  stateMachineUploader.send(Msg.Text("And I look good on the barbecue"))
  cc.waitForInactivity()

  // Validation
  assert(stateMachineUploader.responseCount == 3)
