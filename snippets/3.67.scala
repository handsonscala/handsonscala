> def loop(start: Int, end: Int)(callback: Int => Unit) =
    for i <- Range(start, end) do callback(i)

> loop(start = 5, end = 8): i =>
    println(s"i has value ${i}")
i has value 5
i has value 6
i has value 7
