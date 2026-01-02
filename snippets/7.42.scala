> for branch <- otherBranches do os.call(cmd = ("git", "branch", "-D", branch))

> val gitBranchLines = os.call(cmd = ("git", "branch")).out.lines()
gitBranchLines: Vector[String] = Vector("* main")
