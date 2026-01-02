//| moduleDeps: [FetchLinksAsync.scala]
import scala.concurrent.*, java.util.concurrent.Executors

val service = Executors.newFixedThreadPool(8)
given ec: ExecutionContext = ExecutionContext.fromExecutorService(service)

def fetchAllLinksAsync(startTitle: String, depth: Int): Future[Set[String]] =
  def rec(current: Set[String], seen: Set[String], recDepth: Int): Future[Set[String]] =
    if recDepth >= depth then Future.successful(seen)
    else
      val futures = for title <- current yield fetchLinksAsync(title)
      Future.sequence(futures)
        .map: nextTitleLists =>
          val nextTitles = nextTitleLists.flatten
          rec(nextTitles.filter(!seen.contains(_)), seen ++ nextTitles, recDepth + 1)
        .flatten

  rec(Set(startTitle), Set(startTitle), 0)
