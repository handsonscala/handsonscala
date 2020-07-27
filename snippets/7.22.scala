@ os.copy(
    os.pwd / "newer.txt",
    os.pwd / "newer-2.txt"
  )

@ os.list(os.pwd)
res33: IndexedSeq[os.Path] = ArraySeq(
  /Users/lihaoyi/test/.gitignore,
  /Users/lihaoyi/test/post,
  /Users/lihaoyi/test/newer-2.txt,
  /Users/lihaoyi/test/newer.txt
)
