override def run(msg: Msg): Unit = {
  println(s"$state + $msg -> ")
  super.run(msg)
  println(state)
}
