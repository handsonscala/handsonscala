@ os.move(
    os.pwd / "new.txt",
    os.pwd / "newer.txt"
  )

@ os.list(os.pwd)
res32: IndexedSeq[os.Path] = ArraySeq(
  /Users/lihaoyi/test/.gitignore,
  /Users/lihaoyi/test/post,
  /Users/lihaoyi/test/newer.txt
)
