> def splitDate(s: String) = s match
    case s"$day-$month-$year" =>
      s"day: $day, month: $month, year: $year"
    case _ => "not a date"

> splitDate("9-8-1965")
res12: String = "day: 9, month: 8, year: 1965"

> splitDate("9-8")
res13: String = "not a date"
