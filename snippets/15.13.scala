@ ctx.run(query[City])
cmd7.sc:1: SELECT x.id, x.name, x.countrycode, x.district, x.population FROM city x
res7: List[City] = List(
  City(1, "Kabul", "AFG", "Kabol", 1780000),
  City(2, "Qandahar", "AFG", "Qandahar", 237500),
  City(3, "Herat", "AFG", "Herat", 186800),
  City(4, "Mazar-e-Sharif", "AFG", "Balkh", 127800),
...
