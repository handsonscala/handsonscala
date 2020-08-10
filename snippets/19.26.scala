@ {
  sealed trait Expr
  case class BinOp(left: Expr, op: String, right: Expr) extends Expr
  case class Number(Value: Int) extends Expr
  }
