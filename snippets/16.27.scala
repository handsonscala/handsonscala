override def run(msg: Msg): Unit = {
  if (...) println(s"$state + $msg -> ")
  super.run(msg)
  if (...) println(state)
}
