import $file.FetchLinksAsync, FetchLinksAsync._
import scala.concurrent._, java.util.concurrent.Executors
implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))

def fetchAllLinksAsync(startTitle: String, depth: Int): Future[Set[String]] = {
  def rec(current: Set[String], seen: Set[String], recDepth: Int): Future[Set[String]] = {
    if (recDepth >= depth) Future.successful(seen)
    else {
      val futures = for (title <- current) yield fetchLinksAsync(title)
      Future.sequence(futures).map{nextTitleLists =>
        val nextTitles = nextTitleLists.flatten
        rec(nextTitles.filter(!seen.contains(_)), seen ++ nextTitles, recDepth + 1)
      }.flatten
    }
  }
  rec(Set(startTitle), Set(startTitle), 0)
}
