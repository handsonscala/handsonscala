//| moduleDeps: [FetchLinksAsync.scala]
//| mvnDeps:
//| - com.lihaoyi::castor:0.3.0
import scala.concurrent.*

enum Msg:
  case Start(title: String)
  case Fetch(titles: Seq[String], depth: Int)

class Crawler(maxDepth: Int, complete: Promise[Set[String]], maxConcurrency: Int)
             (using cc: castor.Context) extends castor.SimpleActor[Msg]:
  var seen = Set.empty[String]
  val buffered = collection.mutable.ArrayDeque.empty[(String, Int)]
  var outstanding = 0

  def run(msg: Msg) = msg match
    case Msg.Start(title) => handle(Seq(title), 0)
    case Msg.Fetch(titles, depth) =>
      outstanding -= 1
      handle(titles, depth)

  def handle(titles: Seq[String], depth: Int) =
    while buffered.nonEmpty && outstanding < maxConcurrency do
      val (bufferedTitle, bufferedDepth) = buffered.removeHead()
      fetch(bufferedTitle, bufferedDepth)

    for title <- titles if !seen.contains(title) do
      if depth < maxDepth then
        if outstanding < maxConcurrency then fetch(title, depth)
        else buffered.append(title -> depth)

      pprint.log(title)
      seen += title

    pprint.log((buffered.size, seen.size))
    if outstanding == 0 then complete.success(seen)

  def fetch(title: String, depth: Int) =
    outstanding += 1
    this.sendAsync(fetchLinksAsync(title).map(Msg.Fetch(_, depth + 1)))

def fetchAllLinksAsync(startTitle: String, depth: Int, maxConcurrency: Int): Future[Set[String]] =
  val complete = Promise[Set[String]]
  given cc: castor.Context.Test()
  val crawler = Crawler(depth, complete, maxConcurrency)
  crawler.send(Msg.Start(startTitle))
  complete.future
