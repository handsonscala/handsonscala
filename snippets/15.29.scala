@ ctx.run(
    query[City].filter(_.district == "Test County").update(_.district -> "Test Borough")
  )
cmd32.sc:1: UPDATE city SET district = 'Test Borough' WHERE district = 'Test County'
res32: Long = 4L

@ ctx.run(query[City].filter(_.population == 0))
res33: List[City] = List(
  City(10001, "testville", "TSV", "Test Borough", 0),
  City(10002, "testopolis", "TSO", "Test Borough", 0),
  City(10003, "testberg", "TSB", "Test Borough", 0),
  City(10000, "testford", "TST", "Test Borough", 0)
)
