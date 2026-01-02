> case class Person(name: String, title: String)

> def greet(p: Person) = p match
    case Person(s"$first $last", title) =>
      println(s"Hello $title $last")

    case Person(name, title) =>
      println(s"Hello $title $name")

> greet(Person("Haoyi Li", "Mr"))
Hello Mr Li

> greet(Person("Who?", "Dr"))
Hello Dr Who?
