import $file.Blog, Blog._

assert(
  pprint.log(os.read(os.pwd / "out" / "index.html"))
    .contains("""<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css" />""")
)
assert(
  pprint.log(os.read(os.pwd / "out" / "post" / "my-first-post.html"))
    .contains("""<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css" />""")
)
assert(
  pprint.log(os.read(os.pwd / "out" / "post" / "my-second-post.html"))
    .contains("""<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css" />""")
)
assert(
  pprint.log(os.read(os.pwd / "out" / "post" / "my-third-post.html"))
    .contains("""<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.css" />""")
)
