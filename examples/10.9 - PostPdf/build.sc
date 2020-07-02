import $ivy.`com.lihaoyi::scalatags:0.9.1`, scalatags.Text.all._
import $ivy.`com.atlassian.commonmark:commonmark:0.13.1`
import mill._

def mdNameToHtml(name: String) = name.replace(" ", "-").toLowerCase + ".html"

val postInfo = interp.watchValue {
  os.list(os.pwd / "post")
    .map { p =>
      val s"$prefix - $suffix.md" = p.last
      (prefix, suffix, p)
    }
    .sortBy(_._1.toInt)
}

def bootstrap = T{
  os.write(
    T.dest / "bootstrap.css",
    requests.get("https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css")
      .text()
  )
  PathRef(T.dest / "bootstrap.css")
}

def renderMarkdown(s: String) = {
  val parser = org.commonmark.parser.Parser.builder().build()
  val document = parser.parse(s)
  val renderer = org.commonmark.renderer.html.HtmlRenderer.builder().build()
  renderer.render(document)
}
def renderHtmlPage(dest: os.Path, bootstrapUrl: String, contents: Frag*) = {
  os.write(
    dest,
    doctype("html")(
      html(head(link(rel := "stylesheet", href := bootstrapUrl)), body(contents))
    )
  )
  PathRef(dest)
}

def puppeteer = T{
  os.proc("npm", "install", "puppeteer").call(cwd = T.dest, stderr = os.Pipe)
  PathRef(T.dest)
}

def pdfize = T.source(os.pwd / "pdfize.js")

object post extends Cross[PostModule](postInfo.map(_._1):_*)
class PostModule(number: String) extends Module{
  val Some((_, suffix, markdownPath)) = postInfo.find(_._1 == number)
  def path = T.source(markdownPath)
  def preview = T{
    renderMarkdown(os.read.lines(path().path).takeWhile(_.nonEmpty).mkString("\n"))
  }
  def render = T{
    renderHtmlPage(
      T.dest / mdNameToHtml(suffix),
      "../bootstrap.css",
      h1(a(href := "../index.html")("Blog"), " / ", suffix),
      raw(renderMarkdown(os.read(path().path)))
    )
  }

  def pdf = T {
    for(p <- os.list(puppeteer().path)) os.copy.over(p, T.dest / p.last)
    os.copy(bootstrap().path, T.dest / "bootstrap.css")
    os.makeDir(T.dest / "post")
    val htmlPath = T.dest / "post" / render().path.last
    os.copy(render().path, htmlPath)

    val localPdfize = T.dest / pdfize().path.last

    os.copy.over(pdfize().path, localPdfize)
    val s"$baseName.html" = htmlPath.last
    val pdfPath = T.dest / s"$baseName.pdf"
    os.proc("node", localPdfize, htmlPath, pdfPath)
      .call(cwd = T.dest)
    PathRef(pdfPath)
  }

}

def links = T.input{ postInfo.map(_._2) }
val posts = T.sequence(postInfo.map(_._1).map(post(_).render))
val previews = T.sequence(postInfo.map(_._1).map(post(_).preview))

def index = T {
  renderHtmlPage(
    T.dest / "index.html",
    "bootstrap.css",
    h1("Blog"),
    for ((suffix, preview) <- links().zip(previews()))
    yield frag(
      h2(a(href := ("post/" + mdNameToHtml(suffix)))(suffix)),
      raw(preview) // include markdown-generated HTML "raw" without HTML-escaping it
    )
  )
}

def dist = T {
  for (post <- posts()) {
    os.copy(post.path, T.dest / "post" / post.path.last, createFolders = true)
  }
  os.copy(index().path, T.dest / "index.html")
  os.copy(bootstrap().path, T.dest / "bootstrap.css")
  PathRef(T.dest)
}

val pdfFiles = T.sequence(postInfo.map(_._1).map(post(_).pdf))
def pdfs = T {
  for (pdf <- pdfFiles()) os.copy.into(pdf.path, T.dest)
  PathRef(T.dest)
}
