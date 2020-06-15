import $file.Jsonnet, Jsonnet.jsonnet

assert(
  pprint.log(jsonnet(
    """local greeting = "Hello ";
       local bonus = 15000;
       local person = function (name, baseSalary) {
         "name": name,
         "welcome": greeting + name + "!",
         "totalSalary": baseSalary + bonus
       };
       {
         "person1": person("Alice", 50000),
         "person2": person("Bob", 60000),
         "person3": person("Charlie", 70000)
       }"""
  )) ==
  """{"person1": {"name": "Alice", "welcome": "Hello Alice!", "totalSalary": 65000}, "person2": {"name": "Bob", "welcome": "Hello Bob!", "totalSalary": 75000}, "person3": {"name": "Charlie", "welcome": "Hello Charlie!", "totalSalary": 85000}}"""
)
