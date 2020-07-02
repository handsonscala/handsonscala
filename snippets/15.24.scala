@ ctx.run(
    query[City]
      .join(query[Country])
      .on(_.countryCode == _.code)
      .filter{case (city, country) => country.continent == "Asia"}
      .map{case (city, country) => city.name}
  )
cmd22.sc:1: SELECT x01.name FROM city x01 INNER JOIN country x11
            ON x01.countrycode = x11.code WHERE x11.continent = 'Asia'
res22: List[String] = List(
  "Kabul",
  "Qandahar",
  "Herat",
  "Mazar-e-Sharif",
  "Dubai",
  "Abu Dhabi",
...
