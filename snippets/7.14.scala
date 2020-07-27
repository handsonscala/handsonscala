@ val githubPath = os.Path("/Users/lihaoyi/Github"); val usersPath = os.Path("/Users")

@ githubPath.relativeTo(usersPath)
res19: os.RelPath = lihaoyi/Github

@ usersPath.relativeTo(githubPath)
res20: os.RelPath = ../..
