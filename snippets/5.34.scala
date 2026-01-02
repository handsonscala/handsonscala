> def measureTime(f: => Unit) =
    val start = System.currentTimeMillis()
    f
    val end = System.currentTimeMillis()
    println("Evaluation took " + (end - start) + " milliseconds")

> measureTime(new Array[String](10 * 1000 * 1000).hashCode())
Evaluation took 2 milliseconds

> measureTime: // block syntax for calling a method
    new Array[String](100 * 1000 * 1000).hashCode()

Evaluation took 5 milliseconds

> measureTime: // curly brace syntax
    new Array[String](100 * 1000 * 1000).hashCode()

Evaluation took 4 milliseconds
