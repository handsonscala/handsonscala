def gitBranchLines = os.proc("git", "branch").call().out.lines()
pprint.log(gitBranchLines)

val otherBranches = gitBranchLines.collect{case s"  $branchName" => branchName}
for (branch <- otherBranches) os.proc("git", "branch", "-D", branch).call()
pprint.log(otherBranches)