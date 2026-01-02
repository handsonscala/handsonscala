 val fut1 = Future:
   hash("Chinatown.jpg") 

+val result = fut1.flatMap: h1 =>
+  Future:
+    h1.take(5) + hash("ZCenter.jpg").take(5)

+result.foreach(println)