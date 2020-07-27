 val files = Seq(
   "Chinatown.jpg", "ZCenter.jpg",
   "Kresge.jpg", "Memorial.jpg"
 )
-val futs = files.map(s => Future{hash(s)})
-val hashes = futs.map(Await.result(_, Inf))
-val joined = hashes.mkString(" ")
-println(joined)