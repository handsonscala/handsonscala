+def mdNameToHtml(name: String) = name.replace(" ", "-").toLowerCase + ".html"
+
+for ((_, suffix, path) <- postInfo) {
+  val parser = org.commonmark.parser.Parser.builder().build()
+  val document = parser.parse(os.read(path))
+  val renderer = org.commonmark.renderer.html.HtmlRenderer.builder().build()
+  val output = renderer.render(document)
+  os.write(
+    os.pwd / "out" / "post" / mdNameToHtml(suffix),
+    doctype("html")(
+      html(
+        body(
+          h1("Blog", " / ", suffix),
+          raw(output)
+        )
+      )
+    )
+  )
+}