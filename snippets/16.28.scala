override def run(msg: Msg): Unit =
  if ... then println(s"$state + $msg -> ")
  super.run(msg)
  if ... then println(state)
