> Foo(123)
-- [E007] Type Mismatch Error: -------------------------------------------------
1 |Foo(123)
  |    ^^^
  |    Found:    (123 : Int)
  |    Required: String

> new Foo(123).printMsg("hello")
hello123
