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

val tenLanguagesSpokenInMostCities = ctx.run(
  query[City]
    .join(query[CountryLanguage])
    .on { case (city, language) => city.countryCode == language.countryCode }
    .map{case (city, language) => (city.id, language.language)}
    .groupBy { case (city, language) => language }
    .map{case (language, cityLanguages) => (language, cityLanguages.size)}
    .sortBy{case (language, cityCount) => -cityCount}
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

val tenLanguagesSpokenByLargestPopulation = ctx.run(
  query[City]
    .join(query[CountryLanguage])
    .on { case (city, language) => city.countryCode == language.countryCode }
    .map{case (city, language) => (language.language, city.population, language.percentage)}
    .groupBy { case (language, pop, pct) => language }
    .map{case (language, cityLanguages) =>
      (language, cityLanguages.map{case (language, pop, pct) => pop * pct}.sum)
    }
    .sortBy{case (language, personCount) => personCount.map(-_)}
    .take(10)
)

val tenLanguagesSpokenByLargestPopulationRounded =
  tenLanguagesSpokenByLargestPopulation.map{case (lang, num) => (lang, num.get.toLong)}

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
