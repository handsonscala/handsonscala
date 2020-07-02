import $file.FetchLinksAsync, FetchLinksAsync._
import $ivy.`com.lihaoyi::castor:0.1.3`
import scala.concurrent._, ExecutionContext.Implicits.global

sealed trait Msg
case class Start(title: String) extends Msg
case class Fetch(titles: Seq[String], depth: Int) extends Msg

class Crawler(maxDepth: Int,
              complete: Promise[Set[String]],
              downstream: castor.Actor[String])
             (implicit cc: castor.Context) extends castor.SimpleActor[Msg] {
  var seen = Set.empty[String]
  var outstanding = 0
  def run(msg: Msg) = msg match{
    case Start(title) => handle(Seq(title), 0)
    case Fetch(titles, depth) =>
      outstanding -= 1
      handle(titles, depth)
  }
  def handle(titles: Seq[String], depth: Int) = {
    for(title <- titles if !seen.contains(title)) {
      if (depth < maxDepth) {
        downstream.send(title)
        outstanding += 1
        this.sendAsync(fetchLinksAsync(title).map(Fetch(_, depth + 1)))
      }
      pprint.log(title)
      seen += title
    }
    if (outstanding == 0) complete.success(seen)
  }
}

class DiskActor(logPath: os.Path)
               (implicit cc: castor.Context) extends castor.SimpleActor[String]{

  def run(s: String) = os.write.append(logPath, s + "\n", createFolders = true)
}

def fetchAllLinksAsync(startTitle: String, depth: Int): Future[Set[String]] = {

  val complete = Promise[Set[String]]
  implicit val cc = new castor.Context.Test()
  val diskActor = new DiskActor(os.pwd / "log.txt")
  val crawler = new Crawler(depth, complete, diskActor)
  crawler.send(Start(startTitle))
  complete.future
}
