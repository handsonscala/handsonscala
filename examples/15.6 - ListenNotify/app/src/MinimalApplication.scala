package app
import com.impossibl.postgres.api.jdbc.{PGConnection, PGNotificationListener}
import scalatags.Text.all._
object MinimalApplication extends cask.MainRoutes {
  case class Message(name: String, msg: String)
  import com.opentable.db.postgres.embedded.EmbeddedPostgres

  // Start the database on a best-effort basis, in case some other
  // process is already running it
  val server = try Some(EmbeddedPostgres.builder()
    .setDataDirectory(System.getProperty("user.home") + "/data")
    .setCleanDataDirectory(false).setPort(5432)
    .start())
  catch{ case e => None}
  import io.getquill._
  import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
  val pgDataSource = new org.postgresql.ds.PGSimpleDataSource()
  pgDataSource.setUser("postgres")
  val hikariConfig = new HikariConfig()
  hikariConfig.setDataSource(pgDataSource)
  val ctx = new PostgresJdbcContext(LowerCase, new HikariDataSource(hikariConfig))
  ctx.executeAction("CREATE TABLE IF NOT EXISTS message (name text, msg text);")
  import ctx._

  val asyncPgDataSource = new com.impossibl.postgres.jdbc.PGDataSource()
  asyncPgDataSource.setUser("postgres")
  val connection = asyncPgDataSource.getConnection.unwrap(classOf[PGConnection])
  connection.addNotificationListener(new PGNotificationListener() {
    override def notification(processId: Int, channelName: String, payload: String)  = {
      for (conn <- openConnections) conn.send(cask.Ws.Text(messageList().render))
    }
  })
  val listenStmt = connection.createStatement
  listenStmt.executeUpdate("LISTEN msgs")
  listenStmt.close()

  def messages = ctx.run(query[Message].map(m => (m.name, m.msg)))

  var openConnections = Set.empty[cask.WsChannelActor]
  val bootstrap = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"

  @cask.staticResources("/static")
  def staticResourceRoutes() = "static"

  @cask.get("/")
  def hello() = doctype("html")(
    html(
      head(
        link(rel := "stylesheet", href := bootstrap),
        script(src := "/static/app.js")
      ),
      body(
        div(cls := "container")(
          h1("Scala Chat!"),
          div(id := "messageList")(messageList()),
          div(id := "errorDiv", color.red),
          form(onsubmit := "submitForm(); return false")(
            input(`type` := "text", id := "nameInput", placeholder := "User name"),
            input(`type` := "text", id := "msgInput", placeholder := "Write a message!"),
            input(`type` := "submit")
          )
        )
      )
    )
  )

  def messageList() = frag(for ((name, msg) <- messages) yield p(b(name), " ", msg))

  @cask.postJson("/")
  def postChatMsg(name: String, msg: String) = {
    if (name == "") ujson.Obj("success" -> false, "err" -> "Name cannot be empty")
    else if (msg == "") ujson.Obj("success" -> false, "err" -> "Message cannot be empty")
    else {
      ctx.run(query[Message].insert(lift(Message(name, msg))))
      val notifyStmt = connection.createStatement
      notifyStmt.executeUpdate("NOTIFY msgs")
      notifyStmt.close()
      ujson.Obj("success" -> true, "err" -> "")
    }
  }

  @cask.websocket("/subscribe")
  def subscribe() = cask.WsHandler { connection =>
    connection.send(cask.Ws.Text(messageList().render))
    openConnections += connection
    cask.WsActor { case cask.Ws.Close(_, _) => openConnections -= connection }
  }

  initialize()
}
