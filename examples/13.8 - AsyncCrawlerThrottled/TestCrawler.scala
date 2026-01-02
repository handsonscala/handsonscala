//| moduleDeps: [Crawler.scala]
import scala.concurrent.*, duration.Duration.Inf

def main() =
  val depth0Results = Await.result(fetchAllLinksAsync("Singapore", 0, 16), Inf)
  val depth1Results = Await.result(fetchAllLinksAsync("Singapore", 1, 16), Inf)
  val depth2Results = Await.result(fetchAllLinksAsync("Singapore", 2, 16), Inf)
  val depth3Results = Await.result(fetchAllLinksAsync("Singapore", 3, 16), Inf)

  pprint.log(depth0Results)
  pprint.log(depth1Results)
  pprint.log(depth2Results)
  pprint.log(depth3Results)

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
