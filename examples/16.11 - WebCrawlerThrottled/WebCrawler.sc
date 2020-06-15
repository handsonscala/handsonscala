import $file.FetchLinksAsync, FetchLinksAsync._
import $ivy.`com.lihaoyi::castor:0.1.3`
import scala.concurrent._, ExecutionContext.Implicits.global

sealed trait Msg
case class Start(title: String) extends Msg
case class Fetch(titles: Seq[String], depth: Int) extends Msg

class Crawler(maxDepth: Int, complete: Promise[Set[String]], maxConcurrency: Int)
             (implicit cc: castor.Context) extends castor.SimpleActor[Msg] {
  var seen = Set.empty[String]
  val buffered = collection.mutable.ArrayDeque.empty[(String, Int)]
  var outstanding = 0
  def run(msg: Msg) = msg match{
    case Start(title) => handle(Seq(title), 0)
    case Fetch(titles, depth) =>
      outstanding -= 1
      handle(titles, depth)
  }
  def handle(titles: Seq[String], depth: Int) = {
    while(buffered.nonEmpty && outstanding < maxConcurrency){
      val (bufferedTitle, bufferedDepth) = buffered.removeHead()
      fetch(bufferedTitle, bufferedDepth)
    }

    for(title <- titles if !seen.contains(title)) {
      if (depth < maxDepth) {
        if (outstanding < maxConcurrency) fetch(title, depth)
        else buffered.append(title -> depth)
      }
      pprint.log(title)
      seen += title
    }

    pprint.log((buffered.size, seen.size))
    if (outstanding == 0) complete.success(seen)
  }

  def fetch(title: String, depth: Int) = {
    outstanding += 1
    this.sendAsync(fetchLinksAsync(title).map(Fetch(_, depth + 1)))
  }
}

def fetchAllLinksAsync(startTitle: String, depth: Int, maxConcurrency: Int): Future[Set[String]] = {

  val complete = Promise[Set[String]]
  implicit val cc = new castor.Context.Test()
  val crawler = new Crawler(depth, complete, maxConcurrency)
  crawler.send(Start(startTitle))
  complete.future
}
