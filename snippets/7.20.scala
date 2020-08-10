@ os.write(os.pwd / "new.txt", "Hello")

@ os.list(os.pwd)
res30: IndexedSeq[os.Path] = ArraySeq(
  /Users/lihaoyi/test/.gitignore,
  /Users/lihaoyi/test/post,
  /Users/lihaoyi/test/new.txt,
)

@ os.read(os.pwd / "new.txt")
res31: String = "Hello"
