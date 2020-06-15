import mill._, scalalib._
trait SyncModule extends ScalaModule {
  def scalaVersion = "2.13.2"
  def ivyDeps = Agg(
    ivy"com.lihaoyi::upickle:1.1.0",
    ivy"com.lihaoyi::os-lib:0.7.0",
    ivy"com.lihaoyi::os-lib-watch:0.7.0",
    ivy"com.lihaoyi::castor:0.1.3"
  )
}
object sync extends SyncModule{
  def moduleDeps = Seq(shared)
  def resources = T.sources{
    os.copy(agent.assembly().path, T.dest / "agent.jar")
    super.resources() ++ Seq(PathRef(T.dest))
  }
}
object agent extends SyncModule{
  def moduleDeps = Seq(shared)
}
object shared extends SyncModule