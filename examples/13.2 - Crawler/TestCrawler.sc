import $file.Crawler, Crawler._

val depth0Results = pprint.log(fetchAllLinks("Singapore", 0))
val depth1Results = pprint.log(fetchAllLinks("Singapore", 1))
val depth2Results = pprint.log(fetchAllLinks("Singapore", 2))
val depth3Results = pprint.log(fetchAllLinks("Singapore", 3))

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
