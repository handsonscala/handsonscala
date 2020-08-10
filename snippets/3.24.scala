@ def hello(title: String, firstName: String, lastNameOpt: Option[String]) = {
    lastNameOpt match {
      case Some(lastName) => println(s"Hello $title. $lastName")
      case None => println(s"Hello $firstName")
    }
  }

@ hello("Mr", "Haoyi", None)
Hello Haoyi

@ hello("Mr", "Haoyi", Some("Li"))
Hello Mr. Li
