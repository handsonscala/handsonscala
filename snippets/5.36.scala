> val baseUrl = "https://httpbin.org/status"

> retry(max = 5):
    // Only succeeds with 200
    // status 1/3 of the time
    requests.get(s"$baseUrl/200,400,500")
retry #1
retry #2
res16: requests.Response = Response(
  ".../httpbin.org/status/200,400,500",
  statusCode = 200,
  ...
)
