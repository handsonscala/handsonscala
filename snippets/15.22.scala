@ ctx.run(query[Country].map(c => (c.name, c.continent)))
cmd17.sc:1: SELECT c.name, c.continent FROM country c
res17: List[(String, String)] = List(
  ("Afghanistan", "Asia"),
  ("Netherlands", "Europe"),
  ("Netherlands Antilles", "North America"),
...

@ ctx.run(query[Country].map(c => (c.name, c.continent, c.population)))
cmd18.sc:1: SELECT c.name, c.continent, c.population FROM country c
res18: List[(String, String, Int)] = List(
  ("Afghanistan", "Asia", 22720000),
  ("Netherlands", "Europe", 15864000),
  ("Netherlands Antilles", "North America", 217000),
...
