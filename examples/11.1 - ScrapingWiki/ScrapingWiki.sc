import $ivy.`org.jsoup:jsoup:1.12.1`, org.jsoup._

import collection.JavaConverters._

val doc = Jsoup.parse(os.read(os.pwd / "Wikipedia.html"))

pprint.log(doc.title())

val headlines = doc.select("#mp-itn b a")

pprint.log(headlines, height = 5)

val headlineLinks =
  for (headline <- headlines.asScala)
  yield (headline.attr("title"), headline.attr("href"))

assert(
  pprint.log(headlineLinks) ==
  Seq(
    ("Bek Air Flight 2100", "/wiki/Bek_Air_Flight_2100"),
    ("Assassination of Jamal Khashoggi", "/wiki/Assassination_of_Jamal_Khashoggi"),
    (
      "State of the Netherlands v. Urgenda Foundation",
      "/wiki/State_of_the_Netherlands_v._Urgenda_Foundation"
    ),
    ("Portal:Current events", "/wiki/Portal:Current_events"),
    ("Deaths in 2019", "/wiki/Deaths_in_2019"),
    ("Wikipedia:In the news/Candidates", "/wiki/Wikipedia:In_the_news/Candidates")
  )
)

val headlineText = for (headline <- headlines.asScala) yield headline.text

assert(
  pprint.log(headlineText) ==
  Seq(
    "Bek Air Flight 2100",
    "2018 killing",
    "upholds a ruling",
    "Ongoing",
    "Recent deaths",
    "Nominate an article"
  )
)
