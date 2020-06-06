import $file.Blog, Blog._

assert(pprint.log(os.read(os.pwd / "out" / "post" / "my-first-post.html")).contains(" / My First Post</h1>"))
assert(pprint.log(os.read(os.pwd / "out" / "post" / "my-second-post.html")).contains(" / My Second Post</h1>"))
assert(pprint.log(os.read(os.pwd / "out" / "post" / "my-third-post.html")).contains(" / My Third Post</h1>"))
