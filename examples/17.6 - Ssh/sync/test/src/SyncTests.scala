package sync
import utest._
object SyncTests extends TestSuite:
  val remote = "ec2-user@18.143.166.245"
  val tests = Tests:
    def readRemote(p: os.RelPath) =
      os.call(cmd = ("ssh", remote, "cat", p.toString)).out.text()
    test("success"):

      println("INITIALIZING SRC AND DEST")
      val src = os.temp.dir(os.pwd)
      val dest = os.rel / "out/dest"

      os.write(src / "folder1/hello.txt", "HELLO", createFolders = true)
      os.write(src / "folder1/nested/world.txt", "world", createFolders = true)

      println("FIRST SYNC")
      Sync.main(src.toString, dest.toString, remote)

      println("FIRST VALIDATION")
      assert(readRemote(dest / "folder1/hello.txt") == "HELLO")
      assert(readRemote(dest /  "folder1/nested/world.txt") == "world")

      println("UPDATE SRC")
      os.write.over(src / "folder1/hello.txt", "hello")
      os.write.over(src / "folder1/nested/world.txt", "WORLD")

      println("SECOND SYNC")
      Sync.main(src.toString, dest.toString, remote)

      println("SECOND VALIDATION")
      assert(readRemote(dest / "folder1/hello.txt") == "hello")
      assert(readRemote(dest /  "folder1/nested/world.txt") == "WORLD")
