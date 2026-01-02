> def findName(cityId: Int) = db.run(City.select.filter(_.id === cityId).map(_.name))

> findName(3208)
query: SELECT city0.name AS res FROM city city0 WHERE (city0.id = ?)
res14: Seq[String] = Vector("Singapore")

> findName(3209)
query: SELECT city0.name AS res FROM city city0 WHERE (city0.id = ?)
res15: Seq[String] = Vector("Bratislava")
