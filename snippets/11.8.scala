> val doc = Jsoup.connect("https://developer.mozilla.org/en-US/docs/Web/API").get()
doc: org.jsoup.nodes.Document = <!doctype html>
<html lang="en-US" data-theme="light dark" data-renderer="Doc">
 <head>
...

> val links = doc.select("h2#interfaces").nextAll.select("div.index a").asScala
links: mutable.Buffer[org.jsoup.nodes.Element] = Buffer(
  <a href="/en-US/docs/Web/API/AbortController" ...><code>AbortCont...</code></a>,
  <a href="/en-US/docs/Web/API/AbortSignal" ...><code>AbortSignal</code></a>,
...
