
def retry[T](max: Int, delay: Int = 0)(f: => T): T = {
  var tries = 0
  var result: Option[T] = None
  var currentDelay = delay
  while (result == None) {
    try { result = Some(f) }
    catch {case e: Throwable =>
      Thread.sleep(currentDelay)
      currentDelay *= 2
      tries += 1
      if (tries > max) throw e
      else {
        println(s"failed, retry #$tries")
      }
    }
  }
  result.get
}

val httpbin = "https://httpbin.org"

retry(max = 50, delay = 50 /*milliseconds*/) {
  // Only succeeds with a 200 response
  // code 1/3 of the time
  requests.get(
    s"$httpbin/status/200,400,500"
  )
}
