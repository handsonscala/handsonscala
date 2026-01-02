package app
import scalatags.Text.all.*
object MinimalApplication extends cask.MainRoutes:
  self =>
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

  def messageList() = frag(
    for (name, msg) <- synchronized { messages }
    yield p(b(name), " ", msg)
  )

  @cask.postJson("/")
  def postChatMsg(name: String, msg: String) =
    if name == "" then
      ujson.Obj("success" -> false, "err" -> "Name cannot be empty")
    else if msg == "" then
      ujson.Obj("success" -> false, "err" -> "Message cannot be empty")
    else synchronized {
      synchronized{ messages = messages :+ (name -> msg) }
      for conn <- synchronized { openConnections } do
        conn.send(cask.Ws.Text(messageList().render))
      ujson.Obj("success" -> true, "err" -> "")
    }

  @cask.websocket("/subscribe")
  def subscribe() = cask.WsHandler: connection =>
    connection.send(cask.Ws.Text(messageList().render))
    self.synchronized { openConnections += connection }
    cask.WsActor:
      case cask.Ws.Close(_, _) =>
        self.synchronized { openConnections -= connection }

  initialize()
