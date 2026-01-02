import mill.*, scalalib.*

trait SyncModule extends ScalaModule:
  def scalaVersion = "3.8.0-RC4"
  def mvnDeps = Seq(
    mvn"com.lihaoyi::upickle:4.4.2",
    mvn"com.lihaoyi::os-lib:0.11.6"
  )

object shared extends SyncModule

object sync extends SyncModule:
  def moduleDeps = Seq(shared)
  object test extends ScalaTests with TestModule.Utest:
    def mvnDeps = Seq(mvn"com.lihaoyi::utest:0.9.4")

object agent extends SyncModule:
  def moduleDeps = Seq(shared)