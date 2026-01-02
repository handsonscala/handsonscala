//| mvnDeps:
//| - org.jsoup:jsoup:1.21.2
import org.jsoup.*
import scala.jdk.CollectionConverters.*

val indexDoc = Jsoup.connect("https://developer.mozilla.org/en-US/docs/Web/API").get()
val links = indexDoc.select("h2#interfaces").nextAll.select("div.index a").asScala
val linkData = links.map(link => (link.attr("href"), link.attr("title"), link.text))

val articles = for (url, tooltip, name) <- linkData yield
  println("Scraping " + name)
  val doc = Jsoup.connect("https://developer.mozilla.org" + url).get()
  val summary = doc.select("main#content > div > section > p").asScala.headOption match
    case Some(n) => n.text 
    case None => ""

  val methodsAndProperties = doc
    .select("main#content dl dt")
    .asScala
    .map: el =>
      (
        el.text, 
        el.nextElementSibling match 
          case null => ""
          case x => x.text
      )

  (url, tooltip, name, summary, methodsAndProperties)
