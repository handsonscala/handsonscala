 val fut1 = Future{ hash("Chinatown.jpg") }
 val fut2 = Future{ hash("ZCenter.jpg") }
+val zipped = fut1.zip(fut2)
+val joined = zipped.map{
+  case (hash1, hash2) => s"$hash1 $hash2"
+}
+joined.foreach(println)