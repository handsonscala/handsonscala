> println(jsonnet(
  """local greet = "Hello ";
     local person = function(name) {
       "name": name,
       "welcome": greet + name + "!"
     };
     {
       "person1": person("Alice"),
       "person2": person("Bob"),
       "person3": person("Charlie")
     }"""
  ))
