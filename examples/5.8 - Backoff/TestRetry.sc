import $file.Retry, Retry._

val httpbin = "https://httpbin.org"

retry(max = 50, delay = 50 /*milliseconds*/) {
  // Only succeeds with a 200 response
  // code 1/3 of the time
  requests.get(
    s"$httpbin/status/200,400,500"
  )
}
