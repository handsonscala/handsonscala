package app

import utest._

object ExampleTests extends TestSuite {
  def withServer[T](example: cask.main.Main)(f: String => T): T = {
    val server = io.undertow.Undertow.builder
      .addHttpListener(8082, "localhost")
      .setHandler(example.defaultHandler)
      .build
    server.start()
    val res =
      try f("http://localhost:8082")
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

      val response = requests.post(host, data = Map("name" -> "haoyi", "msg" -> "Test Message!"))

      assert(response.text().contains("Scala Chat!"))
      assert(response.text().contains("alice"))
      assert(response.text().contains("Hello World!"))
      assert(response.text().contains("bob"))
      assert(response.text().contains("I am cow, hear me moo"))
      assert(response.text().contains("haoyi"))
      assert(response.text().contains("Test Message!"))
      assert(response.statusCode == 200)
    }
    test("failure") - withServer(MinimalApplication) { host =>
      val response1 = requests.post(host, data = Map("name" -> "haoyi"), check = false)
      assert(response1.statusCode == 400)
      val response2 = requests.post(host, data = Map("name" -> "haoyi", "msg" -> ""))
      assert(response2.text().contains("Message cannot be empty"))
      val response3 = requests.post(host, data = Map("name" -> "", "msg" -> "Test Message!"))
      assert(response3.text().contains("Name cannot be empty"))
    }
  }
}
