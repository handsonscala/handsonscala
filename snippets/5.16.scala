@ case class Person(name: String, title: String)

@ def greet(p: Person) = p match {
    case Person(s"$firstName $lastName", title) => println(s"Hello $title $lastName")
    case Person(name, title) => println(s"Hello $title $name")
  }

@ greet(Person("Haoyi Li", "Mr"))
Hello Mr Li

@ greet(Person("Who?", "Dr"))
Hello Dr Who?
