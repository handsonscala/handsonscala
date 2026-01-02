> db.run(
    City.select
      .join(Country.select)(_.countryCode === _.code)
      .filter((city, country) => country.continent === "Asia")
      .map((city, country) => city.name)
  )
query: SELECT city0.name AS res FROM city city0 JOIN (SELECT country1.code
AS code, country1.continent AS continent FROM country country1) subquery1
ON (city0.countrycode = subquery1.code) WHERE (subquery1.continent = ?)
res16: Seq[String] = Vector(
  "Kabul",
  "Qandahar",
  "Herat",
  "Mazar-e-Sharif",
  "Dubai",
  "Abu Dhabi",
...
