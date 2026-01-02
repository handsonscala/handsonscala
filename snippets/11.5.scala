> import scala.jdk.CollectionConverters.*
> val headlines = doc.select("#mp-itn b a").asScala
headlines: mutable.Buffer[org.jsoup.nodes.Element] = Buffer(
  <a href="/wiki/Bek_Air_Flight_2100" title="Bek Air Flight 2100">Bek Air Flight 2100</a>,
  <a href="/wiki/Assassination_of_..." title="Assassination of ...">2018 killing</a>,
...
