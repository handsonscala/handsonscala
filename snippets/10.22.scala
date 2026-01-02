-for (_, suffix, path) <- postInfo do
+object post extends Cross[PostModule](postInfo.map(_(0)))
+trait PostModule extends Cross.Module[String]:
+  def number = crossValue
+  val Some((_, suffix, markdownPath)) = postInfo.find(_(0) == number)
+  def srcPath = Task.Source(markdownPath)
+  def render = Task:
     val parser = org.commonmark.parser.Parser.builder().build()
-    val document = parser.parse(os.read(srcPath))
+    val document = parser.parse(os.read(srcPath().path))
     val renderer = org.commonmark.renderer.html.HtmlRenderer.builder().build()
     val output = renderer.render(document)
     os.write(
-      os.pwd / "out/post" / mdNameToHtml(suffix),
+      Task.dest / mdNameToHtml(suffix),
       doctype("html")(
         ...
       )
     )
+    PathRef(Task.dest / mdNameToHtml(suffix))