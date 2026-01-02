> db.run(
    City.select
      .filter(c => c.population > 5000000 && c.countryCode === "CHN")
      .map(c => (c.name, c.countryCode, c.district, c.population))
  )
query: SELECT city0.name AS res_0, city0.countrycode AS res_1, ...
res35: Seq[(String, String, String, Int)] = Vector(
  ("Shanghai", "CHN", "Shanghai", 9696300),
  ("Peking", "CHN", "Peking", 7472000),
  ("Chongqing", "CHN", "Chongqing", 6351600),
  ("Tianjin", "CHN", "Tianjin", 5286800)
)
