//| moduleDeps: [FetchLinks.scala]
import scala.concurrent.*, duration.Duration.Inf, java.util.concurrent.Executors
import duration.*
val service = Executors.newFixedThreadPool(8)
given ec: ExecutionContext = ExecutionContext.fromExecutorService(service)
def fetchAllLinksParallel(startTitle: String, depth: Int): Set[String] =
  var seen = Set(startTitle)
  var current = Set(startTitle)

  for i <- Range(0, depth) do
    val futures = for (title <- current) yield Future{ fetchLinks(title) }
    val nextTitleLists = futures.map(Await.result(_, Inf))
    current = nextTitleLists.flatten.filter(!seen.contains(_))
    seen = seen ++ current

  seen