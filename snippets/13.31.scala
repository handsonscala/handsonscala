 val files = Seq(
   "Chinatown.jpg", "ZCenter.jpg",
   "Kresge.jpg", "Memorial.jpg"
 )
+val futs = files.map(s => Future{hash(s)})
+val hashes = Future.sequence(futs)
+val joined = hashes.map(_.mkString(" "))
+joined.foreach(println)