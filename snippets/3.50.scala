@ def increment(i: Int) = i + 1

@ val b = new Box(123)

@ b.update(increment) // Providing a method reference

@ b.update(x => increment(x)) // Explicitly writing out the function literal

@ b.update{x => increment(x)} // Methods taking a single function can be called with {}s

@ b.update(increment(_)) // You can also use the `_` placeholder syntax

@ b.printMsg("result: ")
result: 127
