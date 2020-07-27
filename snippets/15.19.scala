@ ctx.run(query[City].filter(c => c.population > 5000000 && c.countryCode == "CHN"))
cmd13.sc:1: SELECT c.id, c.name, c.countrycode, c.district, c.population
            FROM city c WHERE c.population > 5000000 AND c.countrycode = 'CHN'
res13: List[City] = List(
  City(1890, "Shanghai", "CHN", "Shanghai", 9696300),
  City(1891, "Peking", "CHN", "Peking", 7472000),
  City(1892, "Chongqing", "CHN", "Chongqing", 6351600),
  City(1893, "Tianjin", "CHN", "Tianjin", 5286800)
)
