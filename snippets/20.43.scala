case class Func(params: Seq[String], body: Expr) extends Expr
case class Call(expr: Expr, args: Seq[Expr]) extends Expr
