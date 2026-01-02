//| mvnDeps:
//| - org.postgresql:postgresql:42.7.8
//| - io.zonky.test:embedded-postgres:2.1.1
//| moduleDeps: [City.scala, Country.scala, CountryLanguage.scala]
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres
import scalasql.simple.*, PostgresDialect.*

def main() =
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

  val tenLanguagesSpokenInMostCities = db.run(
    City.select
      .join(CountryLanguage.select)((city, language) => city.countryCode === language.countrycode)
      .map((city, language) => (city.id, language.language))
      .groupBy((city, language) => language)(_.size)
      .sortBy((language, cityCount) => cityCount).desc
      .take(10)
  )

  pprint.log(tenLanguagesSpokenInMostCities)
  assert(
    tenLanguagesSpokenInMostCities ==
    Seq(
      ("Chinese", 1083L),
      ("German", 885L),
      ("Spanish", 881L),
      ("Italian", 857L),
      ("English", 823L),
      ("Japanese", 774L),
      ("Portuguese", 629L),
      ("Korean", 608L),
      ("Polish", 557L),
      ("French", 467L)
    )
  )

  val tenLanguagesSpokenByLargestPopulation = db.run(
    City.select
      .join(CountryLanguage.select)((city, language) => city.countryCode === language.countrycode)
      .map((city, language) => (language.language, language.percentage, city.population))
      .groupBy((language, pct, pop) => language)(agg => agg.sumBy((_, pct, pop) => pct * pop))
      .sortBy((language, personCount) => personCount).desc
      .take(10)
  )

  val tenLanguagesSpokenByLargestPopulationRounded =
    tenLanguagesSpokenByLargestPopulation.map((lang, num) => (lang, num.toLong))

  pprint.log(tenLanguagesSpokenByLargestPopulationRounded)

  assert(
    tenLanguagesSpokenByLargestPopulationRounded ==
    Seq(
      ("Chinese", 16750695023L),
      ("Spanish", 16642150880L),
      ("English", 11116888890L),
      ("Portuguese", 8529540725L),
      ("Japanese", 7776439163L),
      ("Russian", 7208461083L),
      ("Arabic", 6668091278L),
      ("Hindi", 4927731070L),
      ("Korean", 4605782000L),
      ("German", 2848373842L)
    )
  )
