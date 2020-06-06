 object MinimalApplication extends cask.MainRoutes {
+ var messages = Vector(("alice", "Hello World!"), ("bob", "I am cow, hear me moo"))
  val bootstrap = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"

  @cask.get("/")