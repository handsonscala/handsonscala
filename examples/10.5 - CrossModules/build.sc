import mill._

val items = interp.watchValue{ os.list(millSourcePath / "foo").map(_.last) }

object foo extends Cross[FooModule](items:_*)
class FooModule(label: String) extends Module{
  def srcs = T.source(millSourcePath / "src")

  def concat = T{
    os.write(T.dest / "concat.txt",  os.list(srcs().path).map(os.read(_)))
    PathRef(T.dest / "concat.txt")
  }
}
