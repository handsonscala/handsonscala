> def count(name: Option[String]) =
    name.map(_.length).getOrElse(-1)

> count(Some("Haoyi"))
res60: Int = 5

> count(None)
res61: Int = -1
