package app

import utest._
import scala.concurrent._, duration.Duration.Inf
import castor.Context.Simple.global, cask.util.Logger.Console._

object ExampleTests extends TestSuite {
  def withServer[T](example: cask.main.Main)(f: String => T): T = {
    val server = io.undertow.Undertow.builder
      .addHttpListener(8085, "localhost")
      .setHandler(example.defaultHandler)
      .build
    server.start()
    val res =
      try f("http://localhost:8085")
      finally server.stop()
    res
  }

  val tests = Tests {
    test("success") - withServer(MinimalApplication) { host =>

      val success = requests.get(host)
      assert(success.text().contains("Scala Crawler!"))

      val output = collection.mutable.ArrayDeque.empty[String]
      val wsClient = cask.util.WsClient.connect(s"$host/subscribe") {
        case cask.Ws.Text(msg) => output.append(msg)
      }
      wsClient.send(cask.Ws.Text("3 Singapore"))

      Thread.sleep(10000)
      val collection.mutable.ArrayDeque(depth1, res1, depth2, res2, depth3, res3) = output

      assert(depth1.contains("Depth 1"))
      assert(res1.contains("+65"))

      assert(depth2.contains("Depth 2"))
      assert(res2.contains("Telephone numbers in Singapore"))

      assert(depth3.contains("Depth 3"))
      assert(res3.contains("Ayer Rajah"))
      println(res1)
    }
  }
}
