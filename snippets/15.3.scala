@ import com.opentable.db.postgres.embedded.EmbeddedPostgres

@ val server = EmbeddedPostgres.builder().setPort(5432).start()
server: EmbeddedPostgres = EmbeddedPG-dee4bbc5-7e4e-4559-afb8-10155ecff124

@ {
  import io.getquill._
  import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
  val pgDataSource = new org.postgresql.ds.PGSimpleDataSource()
  pgDataSource.setUser("postgres")
  val hikariConfig = new HikariConfig()
  hikariConfig.setDataSource(pgDataSource)
  val ctx = new PostgresJdbcContext(LowerCase, new HikariDataSource(hikariConfig))
  import ctx._
  }
