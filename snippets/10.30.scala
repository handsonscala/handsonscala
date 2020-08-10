 class PostModule(number: String) extends Module{
   val Some((_, suffix, path)) = postInfo.find(_._1 == number)
   def path = T.source(markdownPath)
+  def preview = T{
+    val parser = org.commonmark.parser.Parser.builder().build()
+    val firstPara = os.read.lines(path().path).takeWhile(_.nonEmpty)
+    val document = parser.parse(firstPara.mkString("\n"))
+    val renderer = org.commonmark.renderer.html.HtmlRenderer.builder().build()
+    val output = renderer.render(document)
+    output
+  }
    def render = T{