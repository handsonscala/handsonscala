import $file.Retry, Retry._
retry(max = 50) {
  // Only succeeds with a 200 response
  // code 1/3 of the time
  requests.get(
    s"$httpbin/status/200,400,500"
  )
}
