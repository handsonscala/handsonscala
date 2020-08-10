@ def nameLength(name: Option[String]) = {
    name.map(_.length).getOrElse(-1)
  }

@ nameLength(Some("Haoyi"))
res60: Int = 5

@ nameLength(None)
res61: Int = -1
