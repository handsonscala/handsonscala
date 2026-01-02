> os.copy(
    os.pwd / "newer.md",
    os.pwd / "newer-2.md"
  )

> os.list(os.pwd)
res29: IndexedSeq[os.Path] = ArraySeq(
  /Users/lihaoyi/test/.gitignore,
  /Users/lihaoyi/test/post,
  /Users/lihaoyi/test/newer-2.md,
  /Users/lihaoyi/test/newer.md
)
