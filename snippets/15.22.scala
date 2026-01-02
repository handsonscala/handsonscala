> db.run(Country.select.map(c => (c.name, c.continent)))
query: SELECT country0.name AS res_0, country0.continent AS res_1
FROM country country0
res12: Seq[(String, String)] = Vector(
  ("Afghanistan", "Asia"),
  ("Netherlands", "Europe"),
  ("Netherlands Antilles", "North America"),
...

> db.run(Country.select.map(c => (c.name, c.continent, c.population)))
query: SELECT country0.name AS res_0, country0.continent AS res_1,
country0.population AS res_2 FROM country country0
res13: Seq[(String, String, Int)] = Vector(
  ("Afghanistan", "Asia", 22720000),
  ("Netherlands", "Europe", 15864000),
  ("Netherlands Antilles", "North America", 217000),
...
