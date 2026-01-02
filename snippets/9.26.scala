//| mvnDeps:
//| - com.lihaoyi::scalatags:0.13.1
//| - org.commonmark:commonmark:0.26.0
import scalatags.Text.all.*

def main(targetGitRepo: String = "") =
  val postInfo = os
    .list(os.pwd / "post")
    .map: p =>
      val s"$prefix - $suffix.md" = p.last
      (prefix, suffix, p)
    .sortBy(_(0).toInt)

  def mdNameToHtml(name: String) =
    name.replace(" ", "-").toLowerCase + ".html"

  val bootstrapCss = link(
    rel := "stylesheet",
    href := "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css"
  )

  os.remove.all(os.pwd / "out")
  os.makeDir.all(os.pwd / "out/post")

  for (_, suffix, path) <- postInfo do
    val parser = org.commonmark.parser.Parser.builder().build()
    val document = parser.parse(os.read(path))
    val renderer = org.commonmark.renderer.html.HtmlRenderer.builder().build()
    val output = renderer.render(document)
    os.write(
      os.pwd / "out/post" / mdNameToHtml(suffix),
      doctype("html")(
        html(
          head(bootstrapCss),
          body(
            h1(a(href := "../index.html")("Blog"), " / ", suffix),
            raw(output)
          )
        )
      )
    )

  os.write(
    os.pwd / "out/index.html",
    doctype("html")(
      html(
        head(bootstrapCss),
        body(
          h1("Blog"),
          for (_, suffix, _) <- postInfo
          yield h2(a(href := ("post/" + mdNameToHtml(suffix)), suffix))
        )
      )
    )
  )

  if targetGitRepo != "" then
    os.call(cmd = ("git", "init"), cwd = os.pwd / "out")
    os.call(cmd = ("git", "add", "-A"), cwd = os.pwd / "out")
    os.call(cmd = ("git", "commit", "-am", "."), cwd = os.pwd / "out")
    os.call(cmd = ("git", "push", targetGitRepo, "head", "-f"), cwd = os.pwd / "out")