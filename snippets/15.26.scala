@ val cities = List(
    City(10001, "testville", "TSV", "Test County", 0)  ,
    City(10002, "testopolis", "TSO", "Test County", 0),
    City(10003, "testberg", "TSB", "Test County", 0)
  )

@ ctx.run(liftQuery(cities).foreach(e => query[City].insert(e)))
cmd26.sc:1: INSERT INTO city (id,name,countrycode,district,population)
            VALUES (?, ?, ?, ?, ?)
res26: List[Long] = List(1L, 1L, 1L)

@ ctx.run(query[City].filter(_.population == 0))
res27: List[City] = List(
  City(10000, "test", "TST", "Test County", 0),
  City(10001, "testville", "TSV", "Test County", 0),
  City(10002, "testopolis", "TSO", "Test County", 0),
  City(10003, "testberg", "TSB", "Test County", 0)
)
