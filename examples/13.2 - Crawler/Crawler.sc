import $file.FetchLinks, FetchLinks._
def fetchAllLinks(startTitle: String, depth: Int): Set[String] = {
  var seen = Set(startTitle)
  var current = Set(startTitle)
  for (i <- Range(0, depth)) {
    val nextTitleLists = for (title <- current) yield fetchLinks(title)
    current = nextTitleLists.flatten.filter(!seen.contains(_))
    seen = seen ++ current
  }
  seen
}
