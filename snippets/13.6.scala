@ val f1 = Future{ "hello" + 123 + "world" }
f1: Future[String] = Future(Success(hello123world))

@ val f2 = Future{ hash("Chinatown.jpg") }
f2: Future[String] = Future(<not completed>)
