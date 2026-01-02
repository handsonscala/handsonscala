> db.run(City.update(_.id === 10000).set(_.name := "testham"))
query: UPDATE city SET name = ? WHERE (city.id = ?)
res21: Int = 1

> db.run(City.select.filter(_.id === 10000).map(c => (c.id, c.name)))
query: SELECT city0.id AS res_0, city0.name AS res_1, ... WHERE (city0.id = ?)
res22: Seq[(Int, String)] = Vector((10000, "testham"))
