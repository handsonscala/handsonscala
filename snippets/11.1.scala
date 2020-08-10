@ val doc = Jsoup.connect("http://en.wikipedia.org/").get()

@ doc.title()
res2: String = "Wikipedia, the free encyclopedia"

@ val headlines = doc.select("#mp-itn b a")
headlines: select.Elements =
<a href="/wiki/Bek_Air_Flight_2100" title="Bek Air Flight 2100">Bek Air Flight 2100</a>
<a href="/wiki/Assassination_of_..." title="Assassination of ...">2018 killing</a>
<a href="/wiki/State_of_the_..." title="State of the...">upholds a ruling</a>
...
