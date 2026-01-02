> class Foo(val value: Int)

> def bar(using foo: Foo) = foo.value + 10
