-for ((_, suffix, path) <- postInfo) {
+object post extends Cross[PostModule](postInfo.map(_._1):_*)
+class PostModule(number: String) extends Module{
+  val Some((_, suffix, markdownPath)) = postInfo.find(_._1 == number)
+  def path = T.source(markdownPath)
+  def render = T{
     val parser = org.commonmark.parser.Parser.builder().build()
-    val document = parser.parse(os.read(path))
+    val document = parser.parse(os.read(path().path))
     val renderer = org.commonmark.renderer.html.HtmlRenderer.builder().build()
     val output = renderer.render(document)
     os.write(
-      os.pwd / "out" / "post" / mdNameToHtml(suffix),
+      T.dest / mdNameToHtml(suffix),
       doctype("html")(
         ...
       )
     )
+    PathRef(T.dest / mdNameToHtml(suffix))
+  }
 }