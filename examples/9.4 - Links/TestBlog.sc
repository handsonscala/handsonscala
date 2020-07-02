import $file.Blog, Blog._

assert(
  pprint.log(os.read(os.pwd / "out" / "index.html"))
    .contains("""<h2><a href="post/my-first-post.html">My First Post</a></h2>""")
)
assert(
  pprint.log(os.read(os.pwd / "out" / "index.html"))
    .contains("""<h2><a href="post/my-second-post.html">My Second Post</a></h2>""")
)
assert(
  pprint.log(os.read(os.pwd / "out" / "index.html"))
    .contains("""<h2><a href="post/my-third-post.html">My Third Post</a></h2>""")
)
