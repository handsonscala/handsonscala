> val relHello = os.RelPath("../hello")
relHello: os.RelPath = ../hello

> os.home / relHello
res16: os.Path = /Users/hello

> relHello / os.RelPath("post")
res17: relHello.ThisType = ../hello/post
