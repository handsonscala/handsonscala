@ ctx.run(query[City].filter(_.id == 10000).update(_.name -> "testford"))
cmd30.sc:1: UPDATE city SET name = 'testford' WHERE id = 10000
res30: Long = 1L

@ ctx.run(query[City].filter(_.id == 10000))
res31: List[City] = List(City(10000, "testford", "TST", "Test County", 0))
