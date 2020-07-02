@ val doc = Jsoup.connect("https://developer.mozilla.org/en-US/docs/Web/API").get()
doc: nodes.Document = <!doctype html>
<html lang="en" dir="ltr" class="no-js">
 <head prefix="og: http://ogp.me/ns#">
...

@ val links = doc.select("h2#Interfaces").nextAll.select("div.index a").asScala
links: collection.mutable.Buffer[nodes.Element] = Buffer(
  <a href="/en-US/docs/Web/API/ANGLE_instanced_arrays"><code>ANGLE_in...</code></a>,
  <a href="/en-US/docs/Web/API/AbortController"><code>AbortController</code></a>,
  ...
