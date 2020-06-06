import $ivy.`org.jsoup:jsoup:1.12.1`, org.jsoup._
import $ivy.`org.asynchttpclient:async-http-client:2.5.2`
import scala.concurrent._, ExecutionContext.Implicits.global

val asyncHttpClient = org.asynchttpclient.Dsl.asyncHttpClient()

def fetchPageAsync(url: String): Future[String] = {
  val p = Promise[org.asynchttpclient.Response]
  val listenableFut = asyncHttpClient.prepareGet(url).execute()
  listenableFut.addListener(() => p.success(listenableFut.get()), null)
  p.future.map(_.getResponseBody)
}

import collection.JavaConverters._
import scala.concurrent._, ExecutionContext.Implicits.global, duration.Duration.Inf
val indexDoc = Jsoup.connect("https://developer.mozilla.org/en-US/docs/Web/API").get()
val links = indexDoc.select("h2#Interfaces").nextAll.select("div.index a").asScala
val linkData = links.map(link => (link.attr("href"), link.attr("title"), link.text))
val articleGroups = for ((group, i) <- linkData.grouped(16).zipWithIndex) yield {
  println("Scraping group " + i)
  val futures = for((url, tooltip, name) <- group) yield {
    fetchPageAsync("https://developer.mozilla.org" + url).map { txt =>
      val doc = Jsoup.parse(txt)
      val summary = doc.select("article#wikiArticle > p").asScala.headOption match {
        case Some(n) => n.text; case None => ""
      }
      val methodsAndProperties = doc
        .select("article#wikiArticle dl dt")
        .asScala
        .map(el => (el.text, el.nextElementSibling match {case null => ""; case x => x.text}))
      (url, tooltip, name, summary, methodsAndProperties)
    }
  }
  futures.map(Await.result(_, Inf))
}

val articles = articleGroups.flatten.toList
