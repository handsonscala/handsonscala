$ ./mill Blog.scala # Generate the static site, do not deploy it

$ ./mill Blog.scala --target-git-repo git@github.com:lihaoyi/test.git

$ ./mill Blog.scala --help
main
  --target-git-repo <str>
