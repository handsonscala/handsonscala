> val p1 = os.Path("/Users/lihaoyi/Github")
p1: os.Path = /Users/lihaoyi/Github

> val p2 = os.Path("/Users")
p2: os.Path = /Users

> p1.subRelativeTo(p2)
res22: os.SubPath = lihaoyi/Github
