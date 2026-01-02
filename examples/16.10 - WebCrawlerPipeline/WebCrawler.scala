//| moduleDeps: [FetchLinksAsync.scala]
//| mvnDeps:
//| - com.lihaoyi::castor:0.3.0
import scala.concurrent.*

enum Msg:
  case Start(title: String)
  case Fetch(titles: Seq[String], depth: Int)

class Crawler(maxDepth: Int,
              complete: Promise[Set[String]],
              downstream: castor.Actor[String])
             (using cc: castor.Context) extends castor.SimpleActor[Msg]:
  var seen = Set.empty[String]
  var outstanding = 0
  
  def run(msg: Msg) = msg match
    case Msg.Start(title) => handle(Seq(title), 0)
    case Msg.Fetch(titles, depth) =>
      outstanding -= 1
      handle(titles, depth)
  
  def handle(titles: Seq[String], depth: Int) =
    for title <- titles if !seen.contains(title) do
      if depth < maxDepth then
        downstream.send(title)
        outstanding += 1
        this.sendAsync(fetchLinksAsync(title).map(Msg.Fetch(_, depth + 1)))
     
      pprint.log(title)
      seen += title
    
    if outstanding == 0 then complete.success(seen)

class DiskActor(logPath: os.Path)
               (using cc: castor.Context) extends castor.SimpleActor[String]:

  def run(s: String) = os.write.append(logPath, s + "\n", createFolders = true)

def fetchAllLinksAsync(startTitle: String, depth: Int): Future[Set[String]] =
  val complete = Promise[Set[String]]
  given cc: castor.Context.Test()
  val diskActor = DiskActor(os.pwd / "log.txt")
  val crawler = Crawler(depth, complete, diskActor)
  crawler.send(Msg.Start(startTitle))
  complete.future
