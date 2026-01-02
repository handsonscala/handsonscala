given ParseSeq: [T] => (p: StrParser[T]) => StrParser[Seq[T]]:
  def parse(s: String) = s.split(',').toSeq.map(p.parse)
