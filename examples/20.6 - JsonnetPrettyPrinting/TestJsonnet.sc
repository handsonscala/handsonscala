import $file.Jsonnet, Jsonnet.jsonnet

assert(
  pprint.log(jsonnet(
    """local greeting = "Hello ";
       local person = function (name) {
         "name": name,
         "welcome": greeting + name + "!"
       };
       {
         "person1": person("Alice"),
         "person2": person("Bob"),
         "person3": person("Charlie")
       }"""
  )) ==
  """{
    |  "person1": {
    |    "name": "Alice",
    |    "welcome": "Hello Alice!"
    |  },
    |  "person2": {
    |    "name": "Bob",
    |    "welcome": "Hello Bob!"
    |  },
    |  "person3": {
    |    "name": "Charlie",
    |    "welcome": "Hello Charlie!"
    |  }
    |}""".stripMargin
)
