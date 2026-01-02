//| mvnDeps:
//| - com.lihaoyi::scalatags:0.13.1
import scalatags.Text.all.*

def main() =
  val postInfo = os
    .list(os.pwd / "post")
    .map: p =>
      val s"$prefix - $suffix.md" = p.last
      (prefix, suffix, p)
    .sortBy(_(0).toInt)

  os.remove.all(os.pwd / "out")
  os.makeDir.all(os.pwd / "out/post")

  os.write(
    os.pwd / "out/index.html",
    doctype("html")(
      html(
        body(
          h1("Blog"),
          for (_, suffix, _) <- postInfo
          yield h2(suffix)
        )
      )
    )
  )
