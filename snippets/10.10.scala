  import mill._
+ trait FooModule extends Module{
    def srcs = T.source(millSourcePath / "src")
    def resources = T.source(millSourcePath / "resources")

    def concat = T{ ... }
    def compress = T{ ... }
    def zipped = T{ ... }
+ }
+
+ object bar extends FooModule
+ object qux extends FooModule