@ ctx.run(query[CountryLanguage])
cmd9.sc:1: SELECT x.countrycode, x.language, x.isofficial, x.percentage FROM ...
res9: List[CountryLanguage] = List(
  CountryLanguage("AFG", "Pashto", true, 52.4000015),
  CountryLanguage("NLD", "Dutch", true, 95.5999985),
  CountryLanguage("ANT", "Papiamento", true, 86.1999969),
  CountryLanguage("ALB", "Albaniana", true, 97.9000015),
...
