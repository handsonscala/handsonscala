> os.write(os.pwd / "post/ABC.txt", "Hello World")

> sync(os.pwd / "post", os.pwd / "post-copy")

> os.exists(os.pwd / "post-copy/ABC.txt")
res39: Boolean = true

> os.read(os.pwd / "post-copy/ABC.txt")
res40: String = "Hello World"
