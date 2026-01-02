//| moduleDeps: [FetchLinksAsync.scala]
import scala.concurrent.*, java.util.concurrent.Executors
val service = Executors.newFixedThreadPool(8)
given ec: ExecutionContext = ExecutionContext.fromExecutorService(service)
def fetchAllLinksAsync(startTitle: String, maxDepth: Int, maxConcurrency: Int): Future[Set[String]] =
  def rec(current: Seq[(String, Int)], seen: Set[String]): Future[Set[String]] =
    pprint.log((maxDepth, current.size, seen.size))
    if current.isEmpty then Future.successful(seen)
    else
      val (throttled, remaining) = current.splitAt(maxConcurrency)
      val futures =
        for (title, depth) <- throttled
        yield fetchLinksAsync(title).map((_, depth))

      Future.sequence(futures).map: nextTitleLists =>
        val flattened = for
          (titles, depth) <- nextTitleLists
          title <- titles
          if !seen.contains(title) && depth < maxDepth
        yield (title, depth + 1)
        rec(remaining ++ flattened, seen ++ flattened.map(_(0)))
      .flatten
  rec(Seq(startTitle -> 0), Set(startTitle))
