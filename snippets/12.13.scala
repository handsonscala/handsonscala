@ val issueData = for (issue <- nonPullRequests) yield (
    issue("number").num.toInt,
    issue("title").str,
    issue("body").str,
    issue("user")("login").str
  )
issueData: collection.mutable.Buffer[(Int, String, String, String)] = ArrayBuffer(
  (
    272,
    "Custom pickling for sealed hierarchies",
    """Citing the manual, "sealed hierarchies are serialized as tagged values,
...
