> for i <- Range.inclusive(1, 100) do
    if i % 3 == 0 && i % 5 == 0 then println("FizzBuzz")
    else if i % 3 == 0 then println("Fizz")
    else if i % 5 == 0 then println("Buzz")
    else println(i)
