> db.run(City.select.filter(_.population > 9000000))
query: SELECT city0.id AS id, city0.name AS name, ... WHERE (city0.population > ?)
res5: Seq[City] = Vector(
  City(
    id = 206,
    name = "São Paulo",
    countryCode = "BRA",
    district = "São Paulo",
    population = 9968485
  ),
  City(
...
