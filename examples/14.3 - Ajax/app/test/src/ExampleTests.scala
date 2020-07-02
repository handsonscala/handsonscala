package app

import utest._

object ExampleTests extends TestSuite {
  def withServer[T](example: cask.main.Main)(f: String => T): T = {
    val server = io.undertow.Undertow.builder
      .addHttpListener(8083, "localhost")
      .setHandler(example.defaultHandler)
      .build
    server.start()
    val res =
      try f("http://localhost:8083")
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

      val response = requests.post(host, data = ujson.Obj("name" -> "haoyi", "msg" -> "Test Message!"))

      val parsed = ujson.read(response.text())
      assert(parsed("success") == ujson.True)
      assert(parsed("err") == ujson.Str(""))

      val parsedTxt = parsed("txt").str
      assert(parsedTxt.contains("alice"))
      assert(parsedTxt.contains("Hello World!"))
      assert(parsedTxt.contains("bob"))
      assert(parsedTxt.contains("I am cow, hear me moo"))
      assert(parsedTxt.contains("haoyi"))
      assert(parsedTxt.contains("Test Message!"))
      assert(response.statusCode == 200)
    }
    test("failure") - withServer(MinimalApplication) { host =>
      val response1 = requests.post(host, data = ujson.Obj("name" -> "haoyi"), check = false)
      assert(response1.statusCode == 400)
      val response2 = requests.post(host, data = ujson.Obj("name" -> "haoyi", "msg" -> ""))
      assert(
        ujson.read(response2.text()) ==
        ujson.Obj("success" -> false, "err" -> "Message cannot be empty")
      )
      val response3 = requests.post(host, data = ujson.Obj("name" -> "", "msg" -> "Test Message!"))
      assert(
        ujson.read(response3.text()) ==
        ujson.Obj("success" -> false, "err" -> "Name cannot be empty")
      )
    }
    test("javascript") - withServer(MinimalApplication) { host =>
      val response1 = requests.get(host + "/static/app.js")
      assert(response1.text().contains("function submitForm()"))
    }
  }
}
