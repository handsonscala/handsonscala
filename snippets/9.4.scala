val postInfo = os
  .list(os.pwd / "post")
  .map: p =>
    val s"$prefix - $suffix.md" = p.last
    (prefix, suffix, p)
  .sortBy(_(0).toInt)

println("POSTS")
postInfo.foreach(println)