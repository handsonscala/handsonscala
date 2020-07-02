@ os.read.lines.stream(os.pwd / ".gitignore").foreach(println)
target/
*.iml
.idea
.settings
...

@ os.walk.stream(os.pwd).foreach(println)
/Users/lihaoyi/test/.gitignore
/Users/lihaoyi/test/post
/Users/lihaoyi/test/post/Programming Interview.md
/Users/lihaoyi/test/post/Hub
/Users/lihaoyi/test/post/Hub/Search.png
