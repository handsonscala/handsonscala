> db.run(CountryLanguage.select)
query: SELECT countrylanguage0.countrycod... FROM countrylanguage countrylanguage0
res2: Seq[CountryLanguage] = Vector(
  CountryLanguage(
    countrycode = "AFG",
    language = "Pashto",
    isOfficial = true,
    percentage = 52.4
  ),
  CountryLanguage(
    countrycode = "NLD",
    language = "Dutch",
...
