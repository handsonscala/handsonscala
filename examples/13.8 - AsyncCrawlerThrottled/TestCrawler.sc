import $file.Crawler, Crawler._
import scala.concurrent._, duration.Duration.Inf

val depth0Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 0, 16), Inf))
val depth1Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 1, 16), Inf))
val depth2Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 2, 16), Inf))
val depth3Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 3, 16), Inf))

pprint.log(depth0Results.size)
pprint.log(depth1Results.size)
pprint.log(depth2Results.size)
pprint.log(depth3Results.size)

assert(depth0Results == Set("Singapore"))
assert(
  depth1Results ==
  Set(
    "1954 National Service riots",
    "16th Summit of the Non-Aligned Movement",
    "126 Squadron, Republic of Singapore Air Force",
    "+65",
    "1915 Singapore Mutiny",
    "1962 Merger Referendum of Singapore",
    "13th Parliament of Singapore",
    "Singapore",
    "1964 race riots in Singapore",
    "1959 Singaporean general election",
    ".sg"
  )
)

assert(depth1Results.subsetOf(depth2Results))
assert(depth1Results.size < depth2Results.size)

assert(depth2Results.subsetOf(depth3Results))
assert(depth2Results.size < depth3Results.size)
