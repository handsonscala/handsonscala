import mill._

trait FooModule extends Module{
  def srcs = T.source(millSourcePath / "src")

  def concat = T{
    os.write(T.dest / "concat.txt",  os.list(srcs().path).map(os.read(_)))
    PathRef(T.dest / "concat.txt")
  }
}

object bar extends FooModule{
  object inner1 extends FooModule
  object inner2 extends FooModule
}
object wrapper extends Module{
  object qux extends FooModule
}