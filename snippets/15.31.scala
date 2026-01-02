+import scalasql.simple.*, PostgresDialect.*
 object MinimalApplication extends cask.MainRoutes:
-  var messages = Vector(("alice", "Hello World!"), ("bob", "I am cow, hear me moo"))
+  case class Message(name: String, msg: String)
+  object Message extends SimpleTable[Message]
+  import io.zonky.test.db.postgres.embedded.EmbeddedPostgres
+
+  val server = EmbeddedPostgres.builder()
+    .setDataDirectory(System.getProperty("user.home") + "/data")
+    .setCleanDataDirectory(false).setPort(5432)
+    .start()
+
+  val pgDataSource = org.postgresql.ds.PGSimpleDataSource()
+  pgDataSource.setUser("postgres")
+
+  val client = scalasql.DbClient.DataSource(
+    pgDataSource,
+    config = new scalasql.Config {
+      override def nameMapper(v: String) = v.toLowerCase
+    }
+  )
+
+  val db = client.getAutoCommitClientConnection
+  sys.addShutdownHook{ db.close() }
+  db.updateRaw("CREATE TABLE IF NOT EXISTS message (name text, msg text);")

   var openConnections = Set.empty[cask.WsChannelActor]