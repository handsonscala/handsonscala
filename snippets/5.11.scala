@ def indexOfDay(d: String) = d match {
    case "Mon" => 1; case "Tue" => 2
    case "Wed" => 3; case "Thu" => 4
    case "Fri" => 5; case "Sat" => 6
    case "Sun" => 7; case _ => -1
  }

@ indexOfDay("Fri")
res22: Int = 5

@ indexOfDay("???")
res23: Int = -1
