> db.run(City.select.filter(_.name.take(4) === "Sing"))
-- [E008] Not Found Error: -----------------------------------------------------
1 |db.run(City.select.filter(_.name.take(4) === "Sing"))
  |                          ^^^^^^^^^^^
  |value take is not a member of scalasql.core.Expr[String]...
