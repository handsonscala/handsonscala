//| moduleDeps: [ScrapingDocs.scala]

def main() =
  pprint.log(articles)
  assert(articles.exists(_(2) == "AnalyserNode"))
  assert(articles.exists(_(2) == "AbortController"))
  assert(articles.exists(_(2) == "AmbientLightSensor"))
  pprint.log(articles.length)
  assert(1000 < articles.length && articles.length < 1050)
