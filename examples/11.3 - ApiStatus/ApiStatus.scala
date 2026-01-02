//| mvnDeps:
//| - org.jsoup:jsoup:1.21.2

import org.jsoup.*
import scala.jdk.CollectionConverters.*

def adjacentElems(elem: nodes.Element): Seq[nodes.Element] =
  elem.nextElementSibling match
    case null => Seq.empty
    case next if next.tagName == "abbr" => Seq(next) ++ adjacentElems(next)
    case next => adjacentElems(next)

def main() =
  val indexDoc = Jsoup.connect("https://developer.mozilla.org/en-US/docs/Web/API").get()

  val links = indexDoc.select("h2#interfaces").nextAll.select("div.index a").asScala

  val annotationsList =
    for
      link <- links
    yield (
      link.text,
      adjacentElems(link).map(_.attr("title"))
    )

  val annotationsMap = annotationsList.toMap

  assert(
    annotationsMap("DOMError") ==
    Seq("Deprecated. Not for use in new websites.")
  )
  assert(
    annotationsMap("VRDisplay") ==
    Seq(
      "Non-standard. Check cross-browser support before using.",
      "Deprecated. Not for use in new websites."
    )
  )

  os.write(os.pwd / "annotations.json", upickle.write(annotationsMap))
