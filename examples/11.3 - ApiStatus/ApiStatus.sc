import $ivy.`org.jsoup:jsoup:1.12.1`, org.jsoup._
import collection.JavaConverters._
val indexDoc = Jsoup.connect("https://developer.mozilla.org/en-US/docs/Web/API").get()

val annotationsList =
  for(span <- indexDoc.select("span.indexListRow").asScala)
  yield (
    span.text,
    span.select("span.indexListBadges .icon-only-inline").asScala.map(_.attr("title"))
  )

val annotationsMap = annotationsList.toMap

assert(
  annotationsMap("BatteryManager") ==
  Seq("This is an obsolete API and is no longer guaranteed to work.")
)
assert(
  annotationsMap("BluetoothAdvertisingData") ==
  Seq(
    "This API has not been standardized.",
    "This is an obsolete API and is no longer guaranteed to work."
  )
)

os.write(os.pwd / "annotations.json", upickle.default.write(annotationsMap))
