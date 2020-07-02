@ ctx.run(query[City].filter(_.population > 9000000))
cmd12.sc:1: SELECT x1.id, x1.name, x1.countrycode, x1.district, x1.population
            FROM city x1 WHERE x1.population > 9000000
res12: List[City] = List(
  City(206, "S\u00e3o Paulo", "BRA", "S\u00e3o Paulo", 9968485),
  City(939, "Jakarta", "IDN", "Jakarta Raya", 9604900),
  City(1024, "Mumbai (Bombay)", "IND", "Maharashtra", 10500000),
  City(1890, "Shanghai", "CHN", "Shanghai", 9696300),
  City(2331, "Seoul", "KOR", "Seoul", 9981619),
  City(2822, "Karachi", "PAK", "Sindh", 9269265)
)
