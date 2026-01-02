> val url = "https://api.github.com/repos/com-lihaoyi/cask/releases"

> os.call(cmd = ("curl", "-L", url), stdout = os.pwd / "github.json")
res45: os.CommandResult = CommandResult(
  command = ...,
  exitCode = 0,
  chunks = Vector()
)
