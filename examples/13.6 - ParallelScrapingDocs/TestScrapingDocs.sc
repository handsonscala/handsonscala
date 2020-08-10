import $file.ScrapingDocs, ScrapingDocs._

pprint.log(articles)
assert(articles.exists(_._3 == "AnalyserNode"))
assert(articles.exists(_._3 == "AbortController"))
assert(articles.exists(_._3 == "AmbientLightSensor"))
pprint.log(articles.length)
assert(950 < articles.length && articles.length < 1000)
