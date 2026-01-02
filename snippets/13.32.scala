 val fut1 = Future:
   hash("Chinatown.jpg")

-val h1 = Await.result(fut1, Inf)
-val fut2 = Future:
-  h1.take(5) + hash("ZCenter.jpg").take(5)

-val result = Await.result(fut2, Inf)
-println(result)