import $ivy.`io.getquill::quill-jdbc:3.5.0`
import $ivy.`org.postgresql:postgresql:42.2.8`
import $ivy.`com.opentable.components:otj-pg-embedded:0.13.1`
import $file.City, City.City
import $file.Country, Country.Country
import $file.CountryLanguage, CountryLanguage.CountryLanguage
import com.opentable.db.postgres.embedded.EmbeddedPostgres

val server = EmbeddedPostgres.builder().setPort(5432).start()

import io.getquill._
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
val pgDataSource = new org.postgresql.ds.PGSimpleDataSource()
pgDataSource.setUser("postgres")
val config = new HikariConfig()
config.setDataSource(pgDataSource)
val ctx = new PostgresJdbcContext(LowerCase, new HikariDataSource(config))
ctx.executeAction(os.read(os.pwd / "world.sql"))
import ctx._

assert(
  ctx.run(query[City].take(4)) ==
  Seq(
    City(1, "Kabul", "AFG", "Kabol", 1780000),
    City(2, "Qandahar", "AFG", "Qandahar", 237500),
    City(3, "Herat", "AFG", "Herat", 186800),
    City(4, "Mazar-e-Sharif", "AFG", "Balkh", 127800),
  )
)
assert(
  ctx.run(query[Country]).take(1) ==
  Seq(
    Country(
      "AFG",
      "Afghanistan",
      "Asia",
      "Southern and Central Asia",
      652090.0,
      Some(1919),
      22720000,
      Some(45.9000015),
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
  ctx.run(query[CountryLanguage].take(4)) ==
  Seq(
    CountryLanguage("AFG", "Pashto", true, 52.4000015),
    CountryLanguage("NLD", "Dutch", true, 95.5999985),
    CountryLanguage("ANT", "Papiamento", true, 86.1999969),
    CountryLanguage("ALB", "Albaniana", true, 97.9000015),
  )
)

val populationAboveNineMillion = ctx.run(query[City].filter(_.population > 9000000))
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

val bigCitiesInChina = ctx.run(query[City].filter(c => c.population > 5000000 && c.countryCode == "CHN"))
assert(
  pprint.log(bigCitiesInChina) ==
  List(
    City(1890, "Shanghai", "CHN", "Shanghai", 9696300),
    City(1891, "Peking", "CHN", "Peking", 7472000),
    City(1892, "Chongqing", "CHN", "Chongqing", 6351600),
    City(1893, "Tianjin", "CHN", "Tianjin", 5286800)
  )
)
val bigCitiesInChina2 = ctx.run(query[City].filter(_.population > 5000000).filter(_.countryCode == "CHN"))
assert(
  pprint.log(bigCitiesInChina2) ==
  List(
    City(1890, "Shanghai", "CHN", "Shanghai", 9696300),
    City(1891, "Peking", "CHN", "Peking", 7472000),
    City(1892, "Chongqing", "CHN", "Chongqing", 6351600),
    City(1893, "Tianjin", "CHN", "Tianjin", 5286800)
  )
)

def find(cityId: Int) = ctx.run(query[City].filter(_.id == lift(cityId)))

assert(pprint.log(find(3208)) == List(City(3208, "Singapore", "SGP", "\u0096", 4017733)))
assert(pprint.log(find(3209)) == List(City(3209, "Bratislava", "SVK", "Bratislava", 448292)))

def findName(cityId: Int) = ctx.run(query[City].filter(_.id == lift(cityId)).map(_.name))

assert(pprint.log(findName(3208)) == List("Singapore"))
assert(pprint.log(findName(3209)) == List("Bratislava"))

println("Inserting Test City...")
ctx.run(query[City].insert(City(10000, "test", "TST", "Test County", 0)))

val testCityInfo = ctx.run(query[City].filter(_.population == 0))
assert(pprint.log(testCityInfo) == List(City(10000, "test", "TST", "Test County", 0)))

println("Inserting More Test Cities...")
ctx.run(
  liftQuery(List(
    City(10001, "testville", "TSV", "Test County", 0)  ,
    City(10002, "testopolis", "TSO", "Test County", 0),
    City(10003, "testberg", "TSB", "Test County", 0)
  )).foreach(e => query[City].insert(e))
)

val allTestCities = ctx.run(query[City].filter(_.population == 0))
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
ctx.run(query[City].filter(_.id == 10000).update(City(10000, "testham", "TST", "Test County", 0)))

val testhamCityInfo = ctx.run(query[City].filter(_.id == 10000))
assert(pprint.log(testhamCityInfo) == List(City(10000, "testham", "TST", "Test County", 0)))

println("Updating testford City Info")
ctx.run(query[City].filter(_.id == 10000).update(_.name -> "testford"))

val testfordCityInfo = ctx.run(query[City].filter(_.id == 10000))
assert(pprint.log(testfordCityInfo) == List(City(10000, "testford", "TST", "Test County", 0)))

println("Updating all Test County Cities...")
ctx.run(query[City].filter(_.district == "Test County").update(_.district -> "Test Borough"))

val updatedTestCountyCitiesInfo = ctx.run(query[City].filter(_.population == 0))
assert(
  pprint.log(updatedTestCountyCitiesInfo) ==
  List(
    City(10001, "testville", "TSV", "Test Borough", 0),
    City(10002, "testopolis", "TSO", "Test Borough", 0),
    City(10003, "testberg", "TSB", "Test Borough", 0),
    City(10000, "testford", "TST", "Test Borough", 0)
  )
)
