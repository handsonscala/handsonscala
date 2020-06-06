import $ivy.`com.lihaoyi::scalatags:0.9.1`, scalatags.Text.all._

interp.watch(os.pwd / "post")
val postInfo = os
  .list(os.pwd / "post")
  .map{ p =>
    val s"$prefix - $suffix.md" = p.last
    (prefix, suffix, p)
  }
  .sortBy(_._1.toInt)

os.remove.all(os.pwd / "out")
os.makeDir.all(os.pwd / "out" / "post")

os.write(
  os.pwd / "out" / "index.html",
  doctype("html")(
    html(
      body(
        h1("Blog"),
        for ((_, suffix, _) <- postInfo)
        yield h2(suffix)
      )
    )
  )
)
