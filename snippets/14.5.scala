import mill._, scalalib._

object app extends ScalaModule {
  def scalaVersion = "2.13.2"
  def ivyDeps = Agg(
    ivy"com.lihaoyi::cask:0.6.7"
  )
  object test extends Tests {
    def testFrameworks = Seq("utest.runner.Framework")

    def ivyDeps = Agg(
      ivy"com.lihaoyi::utest:0.7.4",
      ivy"com.lihaoyi::requests:0.6.2",
    )
  }
}