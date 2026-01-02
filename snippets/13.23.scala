> def doThing(succeed: Boolean) =
    val p: Promise[String] = Promise[String]
    val f: Future[String] = p.future

    f.onComplete:
      case scala.util.Success(res) => println(s"Success! $res")
      case scala.util.Failure(exception) => println(s"Failure :( $exception")

    if succeed then p.success("Yay!")
    else p.failure(Exception("boom"))

> doThing(succeed = true)
Success! Yay!
res12: scala.concurrent.Promise[String] = Future(Success(Yay!))

> doThing(succeed = false)
Failure :( java.lang.Exception: boom
res13: scala.concurrent.Promise[String] = Future(Failure(java.lang.Exception: boom))
