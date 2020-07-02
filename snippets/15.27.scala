@ ctx.run(
    query[City]
      .filter(_.id == 10000)
      .update(City(10000, "testham", "TST", "Test County", 0))
  )
cmd28.sc:1: UPDATE city SET id = 10000, name = 'testham', countrycode = 'TST',
            district = 'Test County', population = 0 WHERE id = 10000
res28: Long = 1L

@ ctx.run(query[City].filter(_.id == 10000))
res29: List[City] = List(City(10000, "testham", "TST", "Test County", 0))
