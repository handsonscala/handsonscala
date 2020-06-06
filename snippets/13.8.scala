@ val f = Future{ hash("Chinatown.jpg") }
f: Future[String] = Future(<not completed>)

@ Await.result(f, Inf)
res11: String = "$2a$17$O0fnZkDyZ1bsJinOPw.eG.H80jYKe4v1rAF8k5sH9uRue4tma50rK"
