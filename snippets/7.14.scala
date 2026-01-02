> val githubPath = os.Path("/Users/lihaoyi/Github")
githubPath: os.Path = /Users/lihaoyi/Github

> val usersPath = os.Path("/Users")
usersPath: os.Path = /Users

> githubPath.relativeTo(usersPath)
res18: os.RelPath = lihaoyi/Github

> usersPath.relativeTo(githubPath)
res19: os.RelPath = ../..
