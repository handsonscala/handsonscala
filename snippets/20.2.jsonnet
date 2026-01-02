local greeting = "Hello ";
local person = function(name) {
  "name": name,
  "welcome": greeting + name + "!"
};
{
  "person1": person("Alice"),
  "person2": person("Bob"),
  "person3": person("Charlie")
}