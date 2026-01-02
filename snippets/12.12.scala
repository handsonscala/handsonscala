> val nonPullRequests = issues.filter(!_.obj.contains("pull_request"))

> nonPullRequests.length
res7: Int = 303
