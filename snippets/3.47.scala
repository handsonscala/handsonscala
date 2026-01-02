val flattened2 = for
  s <- Array("hello", "world")
  i <- Array(1, 2)
  if s + i != "world2"
yield s + i
