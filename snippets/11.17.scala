$ ./mill ScrapingDocs.scala:repl

> articles.length
res4: Int = 1018

> articles.map(_(4).length).sum
res5: Int = 7566

> os.write.over(os.pwd / "docs.json", upickle.write(articles, indent = 4))

> os.read(os.pwd / "docs.json")
res6: String = """[
    [
        "/en-US/docs/Web/API/AbortController",
        "AbortController",
        "AbortController",
        "The AbortController interface represents a controller object that ...",
...
"""
