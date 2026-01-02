> def genericMethodWithoutImplicit[T](s: String) = parseFromString[T](s)
-- [E172] Type Error: -------------------------------------------------------
1 |def genericMethodWithoutImplicit[T](s: String) = parseFromString[T](s)
  |                                                                      ^
  |No given instance of type StrParser[T] was found for parameter parser of
  |method parseFromString
