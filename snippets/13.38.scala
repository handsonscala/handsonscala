> val (res, ns) = time{ fetchAllLinksParallel("Singapore", 4) }
res: Set[String] = HashSet("Ascension Island", "Baba House", ...)
val ns: scala.concurrent.duration.FiniteDuration = 11358217828 nanoseconds

> val (res, ns) = time{ Await.result(fetchAllLinksAsync("Singapore", 4), Inf)}
res: Set[String] = HashSet("Ascension Island", "Baba House", ...)
val ns: scala.concurrent.duration.FiniteDuration = 2620180174 nanoseconds
