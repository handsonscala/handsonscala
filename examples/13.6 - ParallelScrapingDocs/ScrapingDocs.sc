import $ivy.`org.jsoup:jsoup:1.13.1`, org.jsoup._
import collection.JavaConverters._
import scala.concurrent._, duration.Duration.Inf, java.util.concurrent.Executors
implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))

val indexDoc = Jsoup.connect("https://developer.mozilla.org/en-US/docs/Web/API").get()
val links = indexDoc.select("h2#Interfaces").nextAll.select("div.index a").asScala
val linkData = links.map(link => (link.attr("href"), link.attr("title"), link.text))
val articlesFutures = for ((url, tooltip, name) <- linkData) yield Future{
  println("Scraping " + name)
  val doc = Jsoup.connect("https://developer.mozilla.org" + url).get()
  val summary = doc.select("article#wikiArticle > p").asScala.headOption match {
    case Some(n) => n.text; case None => ""
  }
  val methodsAndProperties = doc
    .select("article#wikiArticle dl dt")
    .asScala
    .map(el => (el.text, el.nextElementSibling match {case null => ""; case x => x.text}))
  (url, tooltip, name, summary, methodsAndProperties)
}

val articles = articlesFutures.map(Await.result(_, Inf))
