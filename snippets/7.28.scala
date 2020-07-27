@ os.read.lines.stream(os.pwd / ".gitignore").collect{case s".$str" => str}.toList
res44: List[String] = List(
  "idea",
  "settings",
  "classpath",
  ...
