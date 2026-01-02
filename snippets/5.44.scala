> given foo: Foo = Foo(1)

> bar // `foo` resolved implicitly
res17: Int = 11

> bar(using foo) // `foo` passed explicitly
res18: Int = 11
