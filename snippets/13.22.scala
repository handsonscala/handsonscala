> time{ fetchAllLinks("Singapore", 3) }._2
res10: scala.concurrent.duration.FiniteDuration = 31061249346 nanoseconds

> time{ fetchAllLinksParallel("Singapore", 3) }._2
res11: scala.concurrent.duration.FiniteDuration = 4569134866 nanoseconds
