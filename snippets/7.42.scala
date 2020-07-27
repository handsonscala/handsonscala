@ for (branch <- otherBranches) os.proc("git", "branch", "-D", branch).call()

@ val gitBranchLines = os.proc("git", "branch").call().out.lines()
gitBranchLines: Vector[String] = Vector("* master")
