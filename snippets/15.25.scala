@ ctx.run(query[City].insert(City(10000, "test", "TST", "Test County", 0)))
cmd23.sc:1: INSERT INTO city (id,name,countrycode,district,population)
            VALUES (10000, 'test', 'TST', 'Test County', 0)
res23: Long = 1L

@ ctx.run(query[City].filter(_.population == 0))
res24: List[City] = List(City(10000, "test", "TST", "Test County", 0))
