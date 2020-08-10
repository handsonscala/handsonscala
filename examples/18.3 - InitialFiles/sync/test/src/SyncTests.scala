package sync
import utest._
object SyncTests extends TestSuite{
  val tests = Tests{
    test("success"){

      println("INITIALIZING SRC AND DEST")
      val src = os.temp.dir(os.pwd / "out")
      os.write(src / "initial1.txt", "initial file 1")
      os.makeDir(src / "initial-folder")
      os.write(src / "initial-folder" / "initial2.txt", "initial file 2")
      os.write(src / "initial-folder" / "initial3.txt", "initial file 3")

      val dest = os.temp.dir(os.pwd / "out")

      val syncThread = new Thread(() => Sync.main(Array(src.toString, dest.toString)))
      syncThread.start()
      Thread.sleep(1000)
      println("FIRST SYNC")
      println("WRITING " + (src / "folder1" / "hello.txt", "HELLO"))
      println("WRITING " + (src / "folder1" / "nested" / "world.txt"))
      os.write(src / "folder1" / "hello.txt", "HELLO", createFolders = true)
      os.write(src / "folder1" / "nested" / "world.txt", "world", createFolders = true)

      Thread.sleep(1000)
      println("FIRST VALIDATION")
      assert(os.read(dest / "initial1.txt") == "initial file 1")
      assert(os.read(dest / "initial-folder" / "initial2.txt") == "initial file 2")
      assert(os.read(dest / "initial-folder" / "initial3.txt") == "initial file 3")
      assert(os.read(dest / "folder1" / "hello.txt") == "HELLO")
      assert(os.read(dest /  "folder1" / "nested" / "world.txt") == "world")

      println("UPDATE SRC")
      os.write.over(src / "folder1" / "hello.txt", "hello")
      os.write.over(src / "folder1" / "nested" / "world.txt", "WORLD")

      Thread.sleep(1000)

      println("SECOND VALIDATION")
      assert(os.read(dest / "folder1" / "hello.txt") == "hello")
      assert(os.read(dest /  "folder1" / "nested" / "world.txt") == "WORLD")
    }
  }
}
