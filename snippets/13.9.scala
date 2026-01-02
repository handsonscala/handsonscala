> val f = Future{ hash("Chinatown.jpg") }
f: scala.concurrent.Future[String] = Future(<not completed>)

> val result = hash("ZCenter.jpg")
result: String = "$2a$17$UiLjZlPjag3oaEaeGA.eG.8KLuk3HS0iqPGaRJPdp1Bjl4zj..."

> val backgroundResult = Await.result(f, Inf)
backgroundResult: String = "$2a$17$O0fnZkDyZ1bsJknuXw.eG.9Mesh9W03ZnVPefg..."
