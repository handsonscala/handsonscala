import $file.FetchLinks, FetchLinks._
import scala.concurrent._, duration.Duration.Inf, java.util.concurrent.Executors
implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))
def fetchAllLinksRec(startTitle: String, depth: Int): Set[String] = {
  def rec(current: Set[String], seen: Set[String], recDepth: Int): Set[String] = {
    if (recDepth >= depth) seen
    else {
      val futures = for (title <- current) yield Future{ fetchLinks(title) }
      val nextTitles = futures.map(Await.result(_, Inf)).flatten
      rec(nextTitles.filter(!seen.contains(_)), seen ++ nextTitles, recDepth + 1)
    }
  }
  rec(Set(startTitle), Set(startTitle), 0)
}