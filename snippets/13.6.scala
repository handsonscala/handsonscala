> val f1 = Future{ "hello" + 123 + "world" }
f1: scala.concurrent.Future[String] = Future(Success(hello123world))

> val f2 = Future{ hash("Chinatown.jpg") }
f2: scala.concurrent.Future[String] = Future(<not completed>)
