@ os.write(os.pwd / "post" / "ABC.txt", "Hello World")

@ sync(os.pwd / "post", os.pwd / "post-copy")

@ os.exists(os.pwd / "post-copy" / "ABC.txt")
res54: Boolean = true

@ os.read(os.pwd / "post-copy" / "ABC.txt")
res55: String = "Hello World"
