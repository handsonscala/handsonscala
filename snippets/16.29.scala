given cc: castor.Context.Test():
  override def reportRun(a: castor.Actor[?],
                         msg: Any,
                         token: castor.Context.Token): Unit =
    println(s"$a <- $msg")
    super.reportRun(a, msg, token)
