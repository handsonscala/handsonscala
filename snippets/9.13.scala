@ import $ivy.`com.atlassian.commonmark:commonmark:0.13.1`

@ val parser = org.commonmark.parser.Parser.builder().build()

@ val document = parser.parse("This is *Sparta*")

@ val renderer = org.commonmark.renderer.html.HtmlRenderer.builder().build()

@ val output = renderer.render(document)
output: String = """<p>This is <em>Sparta</em></p>
"""
