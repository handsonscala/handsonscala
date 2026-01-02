val flattened = for
  i <- Array(1, 2)
  s <- Array("hello", "world")
  if s + i != "world2"
yield s + i
