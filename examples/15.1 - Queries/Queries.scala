//| mvnDeps:
//| - com.lihaoyi::scalasql-simple:0.2.7
//| - org.postgresql:postgresql:42.7.8
//| - io.zonky.test:embedded-postgres:2.1.1
//| moduleDeps: [City.scala, Country.scala, CountryLanguage.scala]

def main() =
  import io.zonky.test.db.postgres.embedded.EmbeddedPostgres
  import scalasql.simple.*, PostgresDialect.*

  val server = EmbeddedPostgres.builder().setPort(5432).start()
  val pgDataSource = org.postgresql.ds.PGSimpleDataSource()
  pgDataSource.setUser("postgres")
  val client = scalasql.DbClient.DataSource(
    pgDataSource,
    config = new scalasql.Config {
      override def logSql(sql: String, file: String, line: Int) = println(s"query: $sql")
      override def nameMapper(v: String) = v.toLowerCase
    }
  )
  val db = client.getAutoCommitClientConnection
  db.updateRaw(os.read(os.pwd/("world.sql")))

  assert(
    db.run(City.select.take(4)) ==
    Seq(
      City(1, "Kabul", "AFG", "Kabol", 1780000),
      City(2, "Qandahar", "AFG", "Qandahar", 237500),
      City(3, "Herat", "AFG", "Herat", 186800),
      City(4, "Mazar-e-Sharif", "AFG", "Balkh", 127800),
    )
  )
  assert(
    db.run(Country.select.take(1)) ==
    Seq(
      Country(
        "AFG",
        "Afghanistan",
        "Asia",
        "Southern and Central Asia",
        652090.0,
        Some(1919),
        22720000,
        Some(45.9),
        Some(5976.00),
        None,
        "Afganistan/Afqanestan",
        "Islamic Emirate",
        Some("Mohammad Omar"),
        Some(1),
        "AF"
      )
    )
  )
  assert(
    pprint.log(db.run(CountryLanguage.select.take(4)): Seq[CountryLanguage]) ==
    Seq(
      CountryLanguage("AFG", "Pashto", true, 52.4),
      CountryLanguage("NLD", "Dutch", true, 95.6),
      CountryLanguage("ANT", "Papiamento", true, 86.2),
      CountryLanguage("ALB", "Albaniana", true, 97.9),
    )
  )

  val populationAboveNineMillion = db.run(City.select.filter(_.population > 9000000))
  assert(
    pprint.log(populationAboveNineMillion) ==
    List(
      City(206, "S\u00e3o Paulo", "BRA", "S\u00e3o Paulo", 9968485),
      City(939, "Jakarta", "IDN", "Jakarta Raya", 9604900),
      City(1024, "Mumbai (Bombay)", "IND", "Maharashtra", 10500000),
      City(1890, "Shanghai", "CHN", "Shanghai", 9696300),
      City(2331, "Seoul", "KOR", "Seoul", 9981619),
      City(2822, "Karachi", "PAK", "Sindh", 9269265)
    )
  )

  val bigCitiesInChina = db.run(City.select.filter(c => c.population > 5000000 && c.countryCode === "CHN"))
  assert(
    pprint.log(bigCitiesInChina) ==
    List(
      City(1890, "Shanghai", "CHN", "Shanghai", 9696300),
      City(1891, "Peking", "CHN", "Peking", 7472000),
      City(1892, "Chongqing", "CHN", "Chongqing", 6351600),
      City(1893, "Tianjin", "CHN", "Tianjin", 5286800)
    )
  )
  val bigCitiesInChina2 = db.run(City.select.filter(_.population > 5000000).filter(_.countryCode === "CHN"))
  assert(
    pprint.log(bigCitiesInChina2) ==
    List(
      City(1890, "Shanghai", "CHN", "Shanghai", 9696300),
      City(1891, "Peking", "CHN", "Peking", 7472000),
      City(1892, "Chongqing", "CHN", "Chongqing", 6351600),
      City(1893, "Tianjin", "CHN", "Tianjin", 5286800)
    )
  )

  def find(cityId: Int) = db.run(City.select.filter(_.id === cityId))

  assert(pprint.log(find(3208)) == List(City(3208, "Singapore", "SGP", "\u0096", 4017733)))
  assert(pprint.log(find(3209)) == List(City(3209, "Bratislava", "SVK", "Bratislava", 448292)))

  def findName(cityId: Int) = db.run(City.select.filter(_.id === cityId).map(_.name))

  assert(pprint.log(findName(3208)) == List("Singapore"))
  assert(pprint.log(findName(3209)) == List("Bratislava"))

  println("Inserting Test City...")
  db.run(City.insert.values(City(10000, "test", "TST", "Test County", 0)))

  val testCityInfo = db.run(City.select.filter(_.population === 0))
  assert(pprint.log(testCityInfo) == List(City(10000, "test", "TST", "Test County", 0)))

  println("Inserting More Test Cities...")
  db.run(
    City.insert.values(
      City(10001, "testville", "TSV", "Test County", 0),
      City(10002, "testopolis", "TSO", "Test County", 0),
      City(10003, "testberg", "TSB", "Test County", 0)
    )
  )

  val allTestCities = db.run(City.select.filter(_.population === 0))
  assert(
    pprint.log(allTestCities) ==
    List(
      City(10000, "test", "TST", "Test County", 0),
      City(10001, "testville", "TSV", "Test County", 0),
      City(10002, "testopolis", "TSO", "Test County", 0),
      City(10003, "testberg", "TSB", "Test County", 0)
    )
  )

  println("Updating testham City Info")
  db.run(City.update(_.id === 10000).set(_.name := "testham"))

  val testhamCityInfo = db.run(City.select.filter(_.id === 10000))
  assert(pprint.log(testhamCityInfo) == List(City(10000, "testham", "TST", "Test County", 0)))

  println("Updating testford City Info")
  db.run(City.update(_.id === 10000).set(_.name := "testford"))

  val testfordCityInfo = db.run(City.select.filter(_.id === 10000))
  assert(pprint.log(testfordCityInfo) == List(City(10000, "testford", "TST", "Test County", 0)))

  println("Updating all Test County Cities...")
  db.run(City.update(_.district === "Test County").set(_.district := "Test Borough"))

  val updatedTestCountyCitiesInfo = db.run(City.select.filter(_.population === 0).sortBy(_.id))
  assert(
    pprint.log(updatedTestCountyCitiesInfo) ==
    List(
      City(10000, "testford", "TST", "Test Borough", 0),
      City(10001, "testville", "TSV", "Test Borough", 0),
      City(10002, "testopolis", "TSO", "Test Borough", 0),
      City(10003, "testberg", "TSB", "Test Borough", 0),
    )
  )
  db.close()
