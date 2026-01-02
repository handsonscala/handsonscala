 val fut1 = Future{ hash("Chinatown.jpg") }
 val fut2 = Future{ hash("ZCenter.jpg") }
-val hash1 = Await.result(fut1, Inf)
-val hash2 = Await.result(fut2, Inf)
-val joined = s"$hash1 $hash2"
-println(joined)