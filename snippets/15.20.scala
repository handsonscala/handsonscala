@ def find(cityId: Int) = ctx.run(query[City].filter(_.id == lift(cityId)))
cmd14.sc:1: SELECT x1.id, x1.name, x1.countrycode, x1.district, x1.population
            FROM city x1 WHERE x1.id = ?

@ find(3208)
res15: List[City] = List(City(3208, "Singapore", "SGP", "\u0096", 4017733))

@ find(3209)
res16: List[City] = List(City(3209, "Bratislava", "SVK", "Bratislava", 448292))
