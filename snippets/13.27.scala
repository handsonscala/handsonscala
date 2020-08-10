 val fut = Future{ hash("Chinatown.jpg") }
+val res = fut.map(_.length)
+res.foreach(println)