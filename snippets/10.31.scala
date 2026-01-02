 trait PostModule extends Cross.Module[String]:
   def number = crossValue
   val Some((_, suffix, path)) = postInfo.find(_(0) == number)
   def path = Task.Source(markdownPath)
+  def preview = Task:
+    val parser = org.commonmark.parser.Parser.builder().build()
+    val firstPara = os.read.lines(path().path).takeWhile(_.nonEmpty)
+    val document = parser.parse(firstPara.mkString("\n"))
+    val renderer = org.commonmark.renderer.html.HtmlRenderer.builder().build()
+    val output = renderer.render(document)
+    output

   def render = Task: