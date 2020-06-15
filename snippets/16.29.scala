implicit val cc = new castor.Context.Test() {
  override def reportRun(a: castor.Actor[_],
                         msg: Any,
                         token: castor.Context.Token): Unit = {
    println(s"$a <- $msg")
    super.reportRun(a, msg, token)
  }
}
