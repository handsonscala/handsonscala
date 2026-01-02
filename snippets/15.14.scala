> db.run(Country.select)
query: SELECT country0.code AS code, country0.name AS nam... FROM country country0
res1: Seq[Country] = Vector(
  Country(
    code = "AFG",
    name = "Afghanistan",
    continent = "Asia",
    region = "Southern and Central Asia",
    surfaceArea = 652090.0,
    indepYear = Some(1919),
    population = 22720000,
...
