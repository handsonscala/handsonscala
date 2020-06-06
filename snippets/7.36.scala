@ os.write.append(os.pwd / "post" / "ABC.txt", "\nI am Cow")

@ sync(os.pwd / "post", os.pwd / "post-copy")

@ os.read(os.pwd / "post-copy" / "ABC.txt")
res58: String = """Hello World
I am Cow"""
