  val (hashes, duration) = time{
-   for (p <- os.list(os.pwd)) yield {
+   val futures = for (p <- os.list(os.pwd)) yield Future{
      println("Hashing " + p)
      hash(p.last)
    }
+   futures.map(Await.result(_, Inf))
  }
