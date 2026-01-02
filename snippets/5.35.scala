> def retry[T](max: Int)(f: => T): T =
    var tries = 0
    var result: Option[T] = None
    while result == None do
      try
        result = Some(f)
      catch case e: Throwable =>
        tries += 1
        if tries > max then throw e
        else
          println(s"retry #$tries")
    result.get
