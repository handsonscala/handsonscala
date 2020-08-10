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
  """{"person1": {"name": "Alice", "welcome": "Hello Alice!"}, "person2": {"name": "Bob", "welcome": "Hello Bob!"}, "person3": {"name": "Charlie", "welcome": "Hello Charlie!"}}"""
)

val error = try { jsonnet("""local f = function(x) y; f("abc")"""); ???} catch{case e => e}
assert(
  pprint.log(error.getMessage) ==
  """Jsonnet error at line 1 column 23
    |at line 1 column 27
    |at line 1 column 1""".stripMargin
)

val error2 = try {
  jsonnet(
  """local f = function(x) y;
    |f("abc")
    |""".stripMargin
  )
  ???
} catch{case e => e}

assert(
  pprint.log(error2.getMessage) ==
  """Jsonnet error at line 1 column 23
    |at line 2 column 2
    |at line 1 column 1""".stripMargin
)
