interp.watch(os.pwd / "post")
val postInfo = os
  .list(os.pwd / "post")
  .map{ p =>
    val s"$prefix - $suffix.md" = p.last
    (prefix, suffix, p)
  }
  .sortBy(_._1.toInt)

assert(
  pprint.log(postInfo.map(t => (t._1, t._2))) ==
  Seq("1" -> "My First Post", "2" -> "My Second Post", "3" -> "My Third Post")
)
