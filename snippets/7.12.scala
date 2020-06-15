@ val helloRelPath = os.RelPath("../hello")

@ os.home / helloRelPath
res16: os.Path = /Users/hello

@ helloRelPath / os.RelPath("post")
res17: os.RelPath = ../hello/post
