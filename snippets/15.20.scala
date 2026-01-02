> def find(cityId: Int) = db.run(City.select.filter(_.id === cityId))

> find(3208)
query: SELECT city0.id AS id, city0.name AS name, city0.co... WHERE (city0.id = ?)
res10: Seq[City] = Vector(
  City(
    id = 3208,
    name = "Singapore",
...

> find(3209)
query: SELECT city0.id AS id, city0.name AS name, city0.co... WHERE (city0.id = ?)
res11: Seq[City] = Vector(
  City(
    id = 3209,
    name = "Bratislava",
...
