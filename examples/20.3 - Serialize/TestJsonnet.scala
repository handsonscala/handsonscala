//| moduleDeps: [Evaluate.scala, Serialize.scala]

def main() =
  def jsonnet(input: String): String =
    serialize(evaluate(fastparse.parse(input, Parser.expr(using _)).get.value, Map.empty))

  val result = jsonnet(
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
  )

  val expected = """{"person1": {"name": "Alice", "welcome": "Hello Alice!"}, "person2": {"name": "Bob", "welcome": "Hello Bob!"}, "person3": {"name": "Charlie", "welcome": "Hello Charlie!"}}"""
  assert(result == expected)
