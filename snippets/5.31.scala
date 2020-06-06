@ def retry[T](max: Int)(f: => T): T = {
    var tries = 0
    var result: Option[T] = None
    while (result == None) {
      try { result = Some(f) }
      catch {case e: Throwable =>
        tries += 1
        if (tries > max) throw e
        else {
          println(s"failed, retry #$tries")
        }
      }
    }
    result.get
  }
