> os.call(cmd = "gzip", stdin = os.pwd / "github.json", stdout = os.pwd / "github.json.gz")
res47: os.CommandResult = CommandResult(
  command = ArraySeq("gzip"),
  exitCode = 0,
  chunks = Vector()
)

> os.call(cmd = ("ls", "-lh", "github.json.gz")).out.text()
res48: String = """-rw-r--r-- 1 lihaoyi staff 47K Jun  3 13:30 github.json.gz
"""
