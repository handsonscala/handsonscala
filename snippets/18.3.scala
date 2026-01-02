import mill.*, scalalib.*

trait SyncModule extends ScalaModule:
  def scalaVersion = "3.8.0-RC4"
  def mvnDeps = Seq(
    mvn"com.lihaoyi::upickle:4.4.2",
    mvn"com.lihaoyi::os-lib:0.11.6",
    mvn"com.lihaoyi::os-lib-watch:0.11.5",
    mvn"com.lihaoyi::castor:0.3.0"
  )

object sync extends SyncModule:
  def moduleDeps = Seq(shared)
  def resources = Task:
    os.copy(agent.assembly().path, Task.dest / "agent.jar")
    super.resources() ++ Seq(PathRef(Task.dest))

  object test extends ScalaTests with TestModule.Utest:
    def mvnDeps = Seq(mvn"com.lihaoyi::utest:0.9.4")

object agent extends SyncModule:
  def moduleDeps = Seq(shared)

object shared extends SyncModule