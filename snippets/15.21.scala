@ ctx.run(query[City].filter(_.name.length == 1))
cmd17.sc:1: Tree 'x$1.name.length()' can't be parsed to 'Ast'
val res17 = ctx.run(query[City].filter(_.name.length == 1))
                                      ^
Compilation Failed
