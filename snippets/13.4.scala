> def time[T](op: => T) =
    val start = System.nanoTime
    val value = op
    val taken = System.nanoTime - start
    (value, duration.FiniteDuration(taken, "nanos"))

> time{ hash("Chinatown.jpg") }
res0: (String, scala.concurrent.duration.FiniteDuration) = (
  "$2a$17$O0fnZkDyZ1bsJknuXw.eG.9Mesh9W03ZnVPefgcTVP7sc2rYBdPb2",
  10942495521 nanoseconds
)
