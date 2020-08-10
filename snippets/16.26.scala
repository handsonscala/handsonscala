stateMachineUploader.send(Text("I am cow"))
// Idle() + Text(I am cow) ->
// Buffering(Vector(I am cow))
stateMachineUploader.send(Text("hear me moo"))
// Buffering(Vector(I am cow)) + Text(hear me moo) ->
// Buffering(Vector(I am cow, hear me moo))
Thread.sleep(100)
// Buffering(Vector(I am cow, hear me moo)) + Flush() ->
// Idle()
