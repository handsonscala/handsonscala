> def myReadJson[T: upickle.Reader](): T =
    print("Enter some JSON: ")
    upickle.read[T](Console.in.readLine())

> myReadJson[Seq[Int]]()
Enter some JSON: [1, 2, 3, 4, 5]
res19: Seq[Int] = List(1, 2, 3, 4, 5)

> myReadJson[Author]()
Enter some JSON: {"login": "Haoyi", "id": 1337, "site_admin": true}
res20: Author = Author("Haoyi", 1337, true)
