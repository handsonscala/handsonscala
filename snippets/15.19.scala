> db.run(City.select.filter(c => c.population > 5000000 && c.countryCode === "CHN"))

query: SELECT city0... WHERE ((city0.population > ?) AND (city0.countrycode = ?))
res9: Seq[City] = Vector(
  City(
    id = 1890,
    name = "Shanghai",
    countryCode = "CHN",
    district = "Shanghai",
    population = 9696300
  ),
  City(
    id = 1891,
    name = "Peking",
    countryCode = "CHN",
    district = "Peking",
    population = 7472000
  ),
  City(
    id = 1892,
    name = "Chongqing",
    countryCode = "CHN",
    district = "Chongqing",
    population = 6351600
  ),
  City(
    id = 1893,
    name = "Tianjin",
    countryCode = "CHN",
    district = "Tianjin",
    population = 5286800
  )
)
