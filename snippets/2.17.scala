package build
import mill.*, scalalib.*

object foo extends ScalaModule {
  def scalaVersion = "3.7.1"
  def mvnDeps = Seq(
    mvn"com.lihaoyi::scalatags:0.13.1",
    mvn"com.lihaoyi::mainargs:0.7.8"
  )

  object test extends ScalaTests {
    def mvnDeps = Seq(mvn"com.lihaoyi::utest:0.9.4")
    def testFramework = "utest.runner.Framework"
  }
}