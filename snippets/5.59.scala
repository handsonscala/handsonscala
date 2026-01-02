given ParseTuple: [T, V] => (p1: StrParser[T], p2: StrParser[V]) => StrParser[(T, V)]:
  def parse(s: String) =
    val Array(left, right) = s.split('=').runtimeChecked
    (p1.parse(left), p2.parse(right))
