for i <- Range.inclusive(1, 100) do
  println(
    if i % 3 == 0 && i % 5 == 0 then "FizzBuzz"
    else if i % 3 == 0 then "Fizz"
    else if i % 5 == 0 then "Buzz"
    else i
  )
