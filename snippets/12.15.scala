@ val commentData = for (comment <- comments) yield (
    comment("issue_url").str match {
      case s"https://api.github.com/repos/lihaoyi/$repo/issues/$id" => id.toInt
    },
    comment("user")("login").str,
    comment("body").str
  )
commentData: collection.mutable.Buffer[(Int, String, String)] = ArrayBuffer(
  (1, "lihaoyi", "Oops, fixed it in trunk, so it'll be fixed next time I publish"),
  (2, "lihaoyi", "Was a mistake, just published it, will show up on maven central..."),
...
