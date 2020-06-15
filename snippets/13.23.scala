@ def doThing(succeed: Boolean) = {
    val p: Promise[String] = Promise[String]
    val f: Future[String] = p.future

    f.onComplete{
      case scala.util.Success(res) => println(s"Success! $res")
      case scala.util.Failure(exception) => println(s"Failure :( $exception")
    }

    if (succeed) p.success("Yay!")
    else p.failure(new Exception("boom"))
  }

@ doThing(succeed = true)
Success! Yay!

@ doThing(succeed = false)
Failure :( java.lang.Exception: boom
