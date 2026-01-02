> def increment(i: Int) = i + 1

> val b = Box(123)

> b.update(increment) // Providing a method reference

> b.update(x => increment(x)) // Explicitly writing out the function literal

> b.update: x => // can pass a lambda without parens or braces
    increment(x)

> b.update(increment(_)) // You can also use the `_` placeholder syntax

> b.printMsg("result: ")
result: 127
