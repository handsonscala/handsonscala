import $file.Blog, Blog._

val indexHtml = pprint.log(os.read(os.pwd / "out" / "index.html"))
assert(indexHtml.contains("<h2>My First Post</h2>"))
assert(indexHtml.contains("<h2>My Second Post</h2>"))
assert(indexHtml.contains("<h2>My Third Post</h2>"))
