retry(max = 50, delay = 100 /*milliseconds*/) {
  requests.get(s"$httpbin/status/200,400,500")
}
