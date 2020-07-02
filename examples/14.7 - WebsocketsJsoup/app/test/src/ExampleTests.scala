package app

import utest._
import scala.concurrent._, duration.Duration.Inf
import castor.Context.Simple.global, cask.util.Logger.Console._
import org.jsoup._
import collection.JavaConverters._

object ExampleTests extends TestSuite {
  def withServer[T](example: cask.main.Main)(f: String => T): T = {
    val server = io.undertow.Undertow.builder
      .addHttpListener(8086, "localhost")
      .setHandler(example.defaultHandler)
      .build
    server.start()
    val res =
      try f("http://localhost:8086")
      finally server.stop()
    res
  }

  val tests = Tests {
    test("success") - withServer(MinimalApplication) { host =>
      var wsPromise = scala.concurrent.Promise[String]
      val wsClient = cask.util.WsClient.connect(s"$host/subscribe") {
        case cask.Ws.Text(msg) => wsPromise.success(msg)
      }

      val jsoupRequest = Jsoup.connect(host)
      val success = jsoupRequest.get()
      assert(success.select("h1").asScala.exists(_.text() == "Scala Chat!"))
      assert(
        success.select("div#messageList > p").asScala.map(_.text()) ==
        Seq(
          "alice Hello World!",
          "bob I am cow, hear me moo"
        )
      )
      assert(jsoupRequest.response().statusCode() == 200)

      val wsMsg = Await.result(wsPromise.future, Inf)

      assert(wsMsg.contains("alice"))
      assert(wsMsg.contains("Hello World!"))
      assert(wsMsg.contains("bob"))
      assert(wsMsg.contains("I am cow, hear me moo"))

      wsPromise = scala.concurrent.Promise[String]
      val response = requests.post(host, data = ujson.Obj("name" -> "haoyi", "msg" -> "Test Message!"))

      val parsed = ujson.read(response.text())
      assert(parsed("success") == ujson.True)
      assert(parsed("err") == ujson.Str(""))

      assert(response.statusCode == 200)
      val wsMsg2 = Await.result(wsPromise.future, Inf)
      assert(wsMsg2.contains("alice"))
      assert(wsMsg2.contains("Hello World!"))
      assert(wsMsg2.contains("bob"))
      assert(wsMsg2.contains("I am cow, hear me moo"))
      assert(wsMsg2.contains("haoyi"))
      assert(wsMsg2.contains("Test Message!"))

      val jsoupRequest2 = Jsoup.connect(host)
      val success2 = jsoupRequest2.get()
      assert(success2.select("h1").asScala.exists(_.text() == "Scala Chat!"))
      assert(
        success2.select("div#messageList > p").asScala.map(_.text()) ==
        Seq(
          "alice Hello World!",
          "bob I am cow, hear me moo",
          "haoyi Test Message!"
        )
      )
      assert(jsoupRequest2.response().statusCode() == 200)
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
