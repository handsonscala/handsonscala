> time{ fetchAllLinks("Singapore", 2) }._2
res8: scala.concurrent.duration.FiniteDuration = 4719789996 nanoseconds

> time{ fetchAllLinksParallel("Singapore", 2) }._2
res9: scala.concurrent.duration.FiniteDuration = 1342978751 nanoseconds
