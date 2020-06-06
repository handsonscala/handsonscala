@ val gitStatus = os.proc("git", "status").call()
gitStatus: os.CommandResult = CommandResult(
  0,
...

@ gitStatus.exitCode
res1: Int = 0

@ gitStatus.out.text()
res2: String = """On branch master
Your branch is up to date with 'origin/master'.
Changes to be committed:
...
