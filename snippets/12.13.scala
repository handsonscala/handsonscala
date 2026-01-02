> val issueData = for issue <- nonPullRequests yield (
    issue("number").num.toInt,
    issue("title").str,
    issue("body").strOpt.getOrElse(""),
    issue("user")("login").str
  )
issueData: mutable.Buffer[(Int, String, String, String)] =
ArrayBuffer(
  (
    685,
    "Maybe performance optimization for upickle.core.LinkedHashMap",
    """Motivation:
The current implementation of `upickle.core.LinkedHashMap` makes use of..."""
...
