package app

import utest._

object ExampleTests extends TestSuite {
  def withServer[T](example: cask.main.Main)(f: String => T): T = {
    val server = io.undertow.Undertow.builder
      .addHttpListener(8081, "localhost")
      .setHandler(example.defaultHandler)
      .build
    server.start()
    val res =
      try f("http://localhost:8081")
      finally server.stop()
    res
  }

  val tests = Tests {
    test("success") - withServer(MinimalApplication) { host =>
      val success = requests.get(host)

      assert(success.text().contains("Scala Chat!"))
      assert(success.text().contains("alice"))
      assert(success.text().contains("Hello World!"))
      assert(success.text().contains("bob"))
      assert(success.text().contains("I am cow, hear me moo"))
      assert(success.statusCode == 200)
    }
  }
}
