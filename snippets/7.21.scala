> os.move(
    os.pwd / "new.md",
    os.pwd / "newer.md"
  )

> os.list(os.pwd)
res28: IndexedSeq[os.Path] = ArraySeq(
  /Users/lihaoyi/test/.gitignore,
  /Users/lihaoyi/test/post,
  /Users/lihaoyi/test/newer.md
)
