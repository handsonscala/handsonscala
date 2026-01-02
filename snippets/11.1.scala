> val doc = Jsoup.connect("http://en.wikipedia.org/").get()

> doc.title()
res0: String = "Wikipedia, the free encyclopedia"

> val headlines = doc.select("#mp-itn b a")
headlines: org.jsoup.select.Elements =
<a href="/wiki/Michel_Devoret" title="Michel Devoret">Michel Devoret</a>
<a href="/wiki/Mary_E._Brunkow" title="Mary E. Brunkow">Mary E. Brunkow</a>
<a href="/wiki/Shimon_Sakaguchi" title="Shimon Sakaguchi">Shimon Sakaguchi</a>
...
