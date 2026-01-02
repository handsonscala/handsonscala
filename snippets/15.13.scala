> db.run(City.select)
query: SELECT city0.id AS id, city0.name AS name, city0.countr... FROM city city0
res0: Seq[City] = Vector(
  City(
    id = 1,
    name = "Kabul",
    countryCode = "AFG",
    district = "Kabol",
    population = 1780000
  ),
  City(
...
