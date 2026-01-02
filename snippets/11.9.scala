> val linkData = links.map(link => (link.attr("href"), link.attr("title"), link.text))

val linkData: scala.collection.mutable.Buffer[(String, String, String)] = ArrayBuffer(
  ("/en-US/docs/Web/API/AbortController", "AbortController", "AbortController"),
  ("/en-US/docs/Web/API/AbortSignal", "AbortSignal", "AbortSignal"),
  (
    "/en-US/docs/Web/API/AbsoluteOrientationSensor",
    "AbsoluteOrientationSensor",
    "AbsoluteOrientationSensor"
  ),
...
