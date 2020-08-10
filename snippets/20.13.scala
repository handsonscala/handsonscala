sealed trait Expr
object Expr{
  case class Str(s: String) extends Expr
  case class Ident(name: String) extends Expr
  case class Plus(left: Expr, right: Expr) extends Expr
  case class Dict(pairs: Map[String, Expr]) extends Expr
  case class Local(name: String, assigned: Expr, body: Expr) extends Expr
  case class Func(argNames: Seq[String], body: Expr) extends Expr
  case class Call(expr: Expr, args: Seq[Expr]) extends Expr
}