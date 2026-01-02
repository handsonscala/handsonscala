> def hi(name: Option[String]) =
    for s <- name do println(s"Hi $s")

> hi(None) // does nothing

> hi(Some("Haoyi"))
Hi Haoyi
