package sync
import utest._
object SyncTests extends TestSuite{
  val tests = Tests{
    test("success"){

      println("INITIALIZING SRC AND DEST")
      val src = os.temp.dir(os.pwd / "out")
      val dest = os.temp.dir(os.pwd / "out")

      os.write(src / "folder1" / "hello.txt", "HELLO", createFolders = true)
      os.write(src / "folder1" / "nested" / "world.txt", "world", createFolders = true)

      println("FIRST SYNC")
      Sync.main(Array(src.toString, dest.toString))

      println("FIRST VALIDATION")
      assert(os.read(dest / "folder1" / "hello.txt") == "HELLO")
      assert(os.read(dest /  "folder1" / "nested" / "world.txt") == "world")

      println("UPDATE SRC")
      os.write.over(src / "folder1" / "hello.txt", "hello")
      os.write.over(src / "folder1" / "nested" / "world.txt", "WORLD")

      println("SECOND SYNC")
      Sync.main(Array(src.toString, dest.toString))

      println("SECOND VALIDATION")
      assert(os.read(dest / "folder1" / "hello.txt") == "hello")
      assert(os.read(dest /  "folder1" / "nested" / "world.txt") == "WORLD")

      println("DELETE SRC FILE")
      os.remove(src / "folder1" / "hello.txt")

      println("DELETE SYNC")
      Sync.main(Array(src.toString, dest.toString))

      println("DELETE VALIDATION")
      assert(!os.exists(dest / "folder1" / "hello.txt"))
      assert(os.read(dest /  "folder1" / "nested" / "world.txt") == "WORLD")
    }
  }
}
