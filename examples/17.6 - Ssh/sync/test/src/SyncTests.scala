package sync
import utest._
object SyncTests extends TestSuite{
  val remote = "ubuntu@54.153.33.113"
  val tests = Tests{
    def readRemote(p: os.RelPath) = {
      os.proc("ssh", remote, "cat", p.toString).call().out.text()
    }
    test("success"){

      println("INITIALIZING SRC AND DEST")
      val src = os.temp.dir(os.pwd / "out")
      val dest = os.rel / "out" / "dest"

      os.write(src / "folder1" / "hello.txt", "HELLO", createFolders = true)
      os.write(src / "folder1" / "nested" / "world.txt", "world", createFolders = true)

      println("FIRST SYNC")
      Sync.main(Array(src.toString, dest.toString, remote))

      println("FIRST VALIDATION")
      assert(readRemote(dest / "folder1" / "hello.txt") == "HELLO")
      assert(readRemote(dest /  "folder1" / "nested" / "world.txt") == "world")

      println("UPDATE SRC")
      os.write.over(src / "folder1" / "hello.txt", "hello")
      os.write.over(src / "folder1" / "nested" / "world.txt", "WORLD")

      println("SECOND SYNC")
      Sync.main(Array(src.toString, dest.toString, remote))

      println("SECOND VALIDATION")
      assert(readRemote(dest / "folder1" / "hello.txt") == "hello")
      assert(readRemote(dest /  "folder1" / "nested" / "world.txt") == "WORLD")
    }
  }
}
