> {
  val gitLog = os.spawn(cmd = ("git", "log"))
  val grepAuthor = os.spawn(cmd = ("grep", "Author: "), stdin = gitLog.stdout)
  val output = grepAuthor.stdout.lines().distinct
  }
output: Vector[String] = Vector(
  "Author: Li Haoyi",
  "Author: Guillaume Galy",
  "Author: Nik Vanderhoof",
...
