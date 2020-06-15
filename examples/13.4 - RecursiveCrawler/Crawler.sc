import $file.FetchLinks, FetchLinks._
import scala.concurrent._, ExecutionContext.Implicits.global, duration.Duration.Inf
def fetchAllLinksRec(startTitle: String, depth: Int): Set[String] = {
  def rec(current: Set[String], seen: Set[String], recDepth: Int): Set[String] = {
    if (recDepth >= depth) seen
    else {
      val futures = for (title <- current) yield Future{ fetchLinks(title) }
      val nextTitleLists = futures.map(Await.result(_, Inf))
      val nextTitles = nextTitleLists.flatten
      rec(nextTitles.filter(!seen.contains(_)), seen ++ nextTitles, recDepth + 1)
    }
  }
  rec(Set(startTitle), Set(startTitle), 0)
}
