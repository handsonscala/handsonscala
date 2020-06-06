@ os.proc("gzip").call(stdin = os.pwd / "github.json", stdout = os.pwd / "github.json.gz")
res9: os.CommandResult = CommandResult(0, ArrayBuffer())

@ os.proc("ls", "-lh", "github.json.gz").call().out.text()
res10: String = """-rw-r--r--  1 lihaoyi  staff    23K Jun  3 13:30 github.json.gz
"""
