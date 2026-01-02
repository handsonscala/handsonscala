> db.run(City.insert.values(City(10000, "test", "TST", "Test County", 0)))
query: INSERT INTO city (id, name, countrycode, district, population) VALUES (?,
?, ?, ?, ?)
res17: Int = 1

> db.run(City.select.filter(_.population === 0))
query: SELECT city0.id AS id, city0.name AS name, ... WHERE (city0.population = ?)
res18: Seq[City] = Vector(
  City(
    id = 10000,
    name = "test",
    countryCode = "TST",
    district = "Test County",
    population = 0
  )
)
