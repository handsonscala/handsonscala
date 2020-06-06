@ val (results, dur) = time { fetchAllLinksParallel("Singapore", 4) }
results: Set[String] = HashSet("Airbus", "10,000 Maniacs", "2004 in film", ...
dur: duration.FiniteDuration = 11358217828 nanoseconds

@ val (results, dur) = time { Await.result(fetchAllLinksAsync("Singapore", 4), Inf) }
results: Set[String] = HashSet("Airbus", "10,000 Maniacs", "2004 in film", ...
dur: duration.FiniteDuration = 2620180174 nanoseconds
