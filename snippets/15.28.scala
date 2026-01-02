> db.run(City.update(_.district === "Test County").set(_.district := "Test Borough"))
query: UPDATE city SET district = ? WHERE (city.district = ?)
res25: Int = 4

> db.run(City.select.filter(_.population === 0).map(c => (c.id, c.name, c.district)))
query: SELECT city0.id AS res_0, city0.name AS ... WHERE (city0.population = ?)
res26: Seq[(Int, String, String)] = Vector(
  (10001, "testville", "Test Borough"),
  (10002, "testopolis", "Test Borough"),
  (10003, "testberg", "Test Borough"),
  (10000, "testham", "Test Borough")
)
