package build
import mill._, scalalib._

object app extends ScalaModule:
  def scalaVersion = "3.8.0-RC4"

  def mvnDeps = Seq(
    mvn"com.lihaoyi::cask:0.11.3",
  )

  object test extends ScalaTests with TestModule.Utest:
    def mvnDeps = Seq(
      mvn"com.lihaoyi::utest::0.9.1",
      mvn"com.lihaoyi::requests::0.9.0",
    )