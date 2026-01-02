  val bootstrap = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"

+ @cask.staticResources("/static")
+ def staticResourceRoutes() = "static"

  @cask.get("/")
  def hello() = doctype("html")(
