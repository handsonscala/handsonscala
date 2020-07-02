package app
import scalatags.Text.all._
import scala.concurrent._, ExecutionContext.Implicits.global, duration.Duration.Inf
object MinimalApplication extends cask.MainRoutes {
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
          h1("Scala Crawler!"),
          form(onsubmit := "submitForm(); return false")(
            input(`type` := "text", id := "searchInput", placeholder := "Enter a Wikipedia title!"),
            input(`type` := "text", id := "depthInput", placeholder := "Enter a search depth"),
            input(`type` := "submit")
          ),
          div(id := "resultDiv"),
          div(id := "errorDiv", color.red),
        )
      )
    )
  )

  def fetchLinks(title: String): Seq[String] = {
    val resp = requests.get(
      "https://en.wikipedia.org/w/api.php",
      params = Seq(
        "action" -> "query",
        "titles" -> title,
        "prop" -> "links",
        "format" -> "json"
      )
    )
    for{
      page <- ujson.read(resp.text())("query")("pages").obj.values.toSeq
      links <- page.obj.get("links").toSeq
      link <- links.arr
    } yield link("title").str
  }

  def fetchAllLinksParallel(startTitle: String,
                            depth: Int,
                            onResults: Set[String] => Unit,
                            onDepth: Int => Unit): Set[String] = {
    var seen = Set(startTitle)
    var current = Set(startTitle)
    for (i <- Range(0, depth)) {
      onDepth(i)
      val futures = for (title <- current) yield Future{ fetchLinks(title) }
      val nextTitleLists = futures.map(Await.result(_, Inf))
      current = nextTitleLists.flatten.filter(!seen.contains(_))
      onResults(current)
      seen = seen ++ current
    }
    seen
  }

  @cask.websocket("/subscribe")
  def subscribe() = cask.WsHandler { connection =>
    cask.WsActor {
      case cask.Ws.Text(s"$depth0 $searchTerm") =>
        fetchAllLinksParallel(
          searchTerm,
          depth0.toInt,
          onResults = results => {
            connection.send(
              cask.Ws.Text(
                div(
                  results
                    .toSeq
                    .flatMap(s => Seq[Frag](" - ", a(href := s"https://en.wikipedia.org/wiki/$s")(s)))
                    .drop(1)
                ).render
              )
            )
          },
          onDepth = i => connection.send(cask.Ws.Text(div(b("Depth ", i + 1)).render))
        )
    }
  }

  initialize()
}
