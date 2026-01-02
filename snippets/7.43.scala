def gitBranchLines = os.call(cmd = ("git", "branch")).out.lines()

def main() =
  pprint.log(gitBranchLines)

  val otherBranches = gitBranchLines.collect{case s"  $branchName" => branchName}
  for branch <- otherBranches do os.call(cmd = ("git", "branch", "-D", branch))
  pprint.log(otherBranches)