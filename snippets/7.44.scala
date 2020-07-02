@ val url = "https://api.github.com/repos/lihaoyi/mill/releases"

@ os.proc("curl", url).call(stdout = os.pwd / "github.json")
res7: os.CommandResult = CommandResult(0, ArraySeq())
