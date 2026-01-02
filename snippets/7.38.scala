> val gitStatus = os.call(cmd = ("git", "status"))
gitStatus: os.CommandResult = CommandResult(
  command = ArraySeq("git", "status"),
  exitCode = 0,
...

> gitStatus.exitCode
res43: Int = 0

> gitStatus.out.text()
res44: String = """On branch main
Your branch is up to date with 'origin/main'.
Changes to be committed:
...
