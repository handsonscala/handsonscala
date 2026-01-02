> os.write(os.pwd / "new.md", "Hi")

> os.list(os.pwd)
res26: IndexedSeq[os.Path] = ArraySeq(
  /Users/lihaoyi/test/.gitignore,
  /Users/lihaoyi/test/post,
  /Users/lihaoyi/test/new.md,
)

> os.read(os.pwd / "new.md")
res27: String = "Hi"
