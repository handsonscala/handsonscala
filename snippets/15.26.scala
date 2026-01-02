> val cities = List(
    City(10001, "testville", "TSV", "Test County", 0)  ,
    City(10002, "testopolis", "TSO", "Test County", 0),
    City(10003, "testberg", "TSB", "Test County", 0)
  )

> db.run(City.insert.values(cities*))
query: INSERT INTO city (id, name, countrycode, district, population) VALUES (?,
?, ?, ?, ?), (?, ?, ?, ?, ?), (?, ?, ?, ?, ?)
res19: Int = 3

> db.run(City.select.filter(_.population === 0).map(c => (c.id, c.name)))
query: SELECT city0.id AS res_0, city0.name AS ... WHERE (city0.population = ?)
res20: Seq[(Int, String)] = Vector(
  (10000, "test"),
  (10001, "testville"),
  (10002, "testopolis"),
  (10003, "testberg")
)
