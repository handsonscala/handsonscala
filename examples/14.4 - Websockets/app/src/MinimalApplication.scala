package app
import scalatags.Text.all._
object MinimalApplication extends cask.MainRoutes {
  var messages = Vector(("alice", "Hello World!"), ("bob", "I am cow, hear me moo"))
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
      messages = messages :+ (name -> msg)
      for (conn <- openConnections) conn.send(cask.Ws.Text(messageList().render))
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
