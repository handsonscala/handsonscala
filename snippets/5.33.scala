@ class Foo(val value: Int)

@ def bar(implicit foo: Foo) = foo.value + 10
