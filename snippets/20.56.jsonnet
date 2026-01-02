local bonus = 15000;
local person = function (name, baseSalary) {
  "name": name,
  "totalSalary": baseSalary + bonus
};
{"person1": person("Alice", 50000)}