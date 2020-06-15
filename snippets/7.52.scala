@ {
  val gitLog = os.proc("git", "log").spawn()
  val grepAuthor = os.proc("grep", "Author: ").spawn(stdin = gitLog.stdout)
  val output = grepAuthor.stdout.lines().distinct
  }
output: Vector[String] = Vector(
  "Author: Li Haoyi",
  "Author: Guillaume Galy",
  "Author: Nik Vanderhoof",
...
