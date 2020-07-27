 val fut = Future{ hash("Chinatown.jpg") }
-val res = Await.result(fut, Inf).length
-println(res)