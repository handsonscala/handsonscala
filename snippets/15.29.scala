> client.transaction: db =>
    db.run(City.update(_.district === "Test Borough").set(_.district := "Test County"))
    throw Exception("oops!")
query: UPDATE city SET district = ? WHERE (city.district = ?)
java.lang.Exception: oops!
  at rs$line$39$.$init$$$anonfun$1(rs$line$39:5)
  at scalasql.core.DbClient$Connection.transaction(DbClient.scala:80)
...

> db.run(City.select.filter(_.population === 0).map(c => (c.id, c.name, c.district)))
res27: Seq[(Int, String, String)] = Vector(
  (10001, "testville", "Test Borough"),
  (10002, "testopolis", "Test Borough"),
  (10003, "testberg", "Test Borough"),
  (10000, "testham", "Test Borough")
)
