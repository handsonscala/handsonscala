@ def hello2(name: Option[String]) = {
    for (s <- name) println(s"Hello $s")
  }

@ hello2(None) // does nothing

@ hello2(Some("Haoyi"))
Hello Haoyi
