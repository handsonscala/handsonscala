> val f = new Foo(1)

> f.printMsg("hello") // `printMsg` uses `x`
hello1

> f.x // Code outside of `Foo` cannot use `x`
-- [E173] Reference Error: -------------
1 |f.x
  |^^^
  |x can only be accessed from Foo.
