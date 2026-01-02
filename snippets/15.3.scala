> import io.zonky.test.db.postgres.embedded.EmbeddedPostgres

> val server = EmbeddedPostgres.builder().setPort(5432).start()
server: io.zonky.test.db.postgres.embedded.EmbeddedPostgres =
EmbeddedPG-7e32d353-dd40-46bf-8dc8-095dc99a0945

> {
  import scalasql.simple.*, PostgresDialect.*
  val pgDataSource = new org.postgresql.ds.PGSimpleDataSource()
  pgDataSource.setUser("postgres")
  val client = scalasql.DbClient.DataSource(
    pgDataSource,
    config = new scalasql.Config {
      override def logSql(sql: String, file: String, line: Int) = println(s"query: $sql")
      override def nameMapper(v: String) = v.toLowerCase
    }
  )
  val db = client.getAutoCommitClientConnection
  }
