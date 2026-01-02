//| moduleDeps: [Crawler.scala, FetchLinksAsync.scala]
def main() =
  import scala.concurrent._, duration.Duration.Inf

  val depth0Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 0), Inf): Set[String])
  val depth1Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 1), Inf): Set[String])
  val depth2Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 2), Inf): Set[String])
  val depth3Results = pprint.log(Await.result(fetchAllLinksAsync("Singapore", 3), Inf): Set[String])

  pprint.log(depth0Results.size)
  pprint.log(depth1Results.size)
  pprint.log(depth2Results.size)
  pprint.log(depth3Results.size)

  try
    assert(depth0Results == Set("Singapore"))
    assert(
      depth1Results ==
      Set(
        "16th Summit of the Non-Aligned Movement",
        "18th Summit of the Non-Aligned Movement",
        "126 Squadron, Republic of Singapore Air Force",
        "+65",
        "10th Summit of the Non-Aligned Movement",
        "Singapore",
        "1819 Singapore Treaty",
        "15th Parliament of Singapore",
        "13 May incident",
        "\"Josip Broz Tito\" Art Gallery of the Nonaligned Countries",
        ".sg"
      )
    )
    assert(depth1Results.subsetOf(depth2Results))
    assert(depth1Results.size < depth2Results.size)
    assert(depth2Results.subsetOf(depth3Results))
    assert(depth2Results.size < depth3Results.size)
  finally
    service.shutdown()
    asyncHttpClient.close()
