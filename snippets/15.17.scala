> db.run(City.select.filter(_.name === "Singapore"))
query: SELECT city0.id AS id, city0.name AS name, city... WHERE (city0.name = ?)
res3: Seq[City] = Vector(
  City(
    id = 3208,
    name = "Singapore",
    countryCode = "SGP",
    district = "",
    population = 4017733
  )
)

> db.run(City.select.filter(_.id === 3208))
query: SELECT city0.id AS id, city0.name AS name, city... WHERE (city0.id = ?)
res4: Seq[City] = Vector(
  City(
    id = 3208,
    name = "Singapore",
...
