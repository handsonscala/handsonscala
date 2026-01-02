> enum Expr:
    case BinOp(left: Expr, op: String, right: Expr)
    case Number(value: Int)
