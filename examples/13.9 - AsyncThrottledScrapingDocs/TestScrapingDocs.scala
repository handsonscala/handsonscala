//| moduleDeps: [ScrapingDocs.scala]

def main() =
  try
    pprint.log(articles)
    assert(articles.exists(_(2) == "AbortController"))
    assert(articles.exists(_(2) == "Accelerometer"))
    assert(articles.exists(_(2) == "AnalyserNode"))
    pprint.log(articles.length)
    assert(1000 < articles.length && articles.length < 1100)
  finally
    service.shutdown()
    asyncHttpClient.close()
