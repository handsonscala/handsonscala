$ ./mill --import "org.commonmark:commonmark:0.26.0" --repl

> val parser = org.commonmark.parser.Parser.builder().build()

> val document = parser.parse("This is *Sparta*")

> val renderer = org.commonmark.renderer.html.HtmlRenderer.builder().build()

> val output = renderer.render(document)
output: String = """<p>This is <em>Sparta</em></p>
"""
