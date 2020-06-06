def futureFunc(): Future[String] = {
  val p = Promise[String]
  callbackFunc(
    onSuccess = str => p.success(str),
    onError = ex => p.failure(ex)
  )
  p.future
}

def callbackFunc(onSuccess: String => Unit, onError: Throwable => Unit): Unit = { ... }
