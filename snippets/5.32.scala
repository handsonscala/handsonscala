@ val httpbin = "https://httpbin.org"

@ retry(max = 5) {
    // Only succeeds with a 200 response
    // code 1/3 of the time
    requests.get(
      s"$httpbin/status/200,400,500"
    )
  }
call failed, retry #1
call failed, retry #2
res68: requests.Response = Response(
  "https://httpbin.org/status/200,400,500",
  200,
...
