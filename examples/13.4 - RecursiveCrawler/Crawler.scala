//| moduleDeps: [FetchLinks.scala]
import scala.concurrent.*, duration.Duration.Inf, java.util.concurrent.Executors
val service = Executors.newFixedThreadPool(8)
given ec: ExecutionContext = ExecutionContext.fromExecutorService(service)

def fetchAllLinksRec(startTitle: String, depth: Int): Set[String] =
  def rec(current: Set[String], seen: Set[String], recDepth: Int): Set[String] =
    if recDepth >= depth then seen
    else
      val futures = for title <- current yield Future{ fetchLinks(title) }
      val nextTitles = futures.map(Await.result(_, Inf)).flatten
      rec(nextTitles.filter(!seen.contains(_)), seen ++ nextTitles, recDepth + 1)

  rec(Set(startTitle), Set(startTitle), 0)
