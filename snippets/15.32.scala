 object MinimalApplication extends cask.MainRoutes {
-   var messages = Vector(("alice", "Hello World!"), ("bob", "I am cow, hear me moo"))
+  case class Message(name: String, msg: String)
+  import com.opentable.db.postgres.embedded.EmbeddedPostgres
+  val server = EmbeddedPostgres.builder()
+    .setDataDirectory(System.getProperty("user.home") + "/data")
+    .setCleanDataDirectory(false).setPort(5432)
+    .start()
+  import io.getquill._
+  import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
+  val pgDataSource = new org.postgresql.ds.PGSimpleDataSource()
+  pgDataSource.setUser("postgres")
+  val hikariConfig = new HikariConfig()
+  hikariConfig.setDataSource(pgDataSource)
+  val ctx = new PostgresJdbcContext(LowerCase, new HikariDataSource(hikariConfig))
+  ctx.executeAction("CREATE TABLE IF NOT EXISTS message (name text, msg text);")
+  import ctx._

   var openConnections = Set.empty[cask.WsChannelActor]