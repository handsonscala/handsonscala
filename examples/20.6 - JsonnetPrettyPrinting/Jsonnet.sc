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
object Parser {
  import fastparse._, MultiLineWhitespace._
  def expr[_: P]: P[Expr] = P( prefixExpr ~ plus.rep ).map{
    case (e, items) => items.foldLeft(e)(Expr.Plus(_, _))
  }
  def prefixExpr[_: P]: P[Expr] = P( callExpr ~ call.rep ).map{
    case (e, items) => items.foldLeft(e)(Expr.Call(_, _))
  }
  def callExpr[_: P] = P( str | dict | local | func | ident )

  def str[_: P] = P( str0 ).map(Expr.Str)
  def str0[_: P] = P( "\"" ~~/ CharsWhile(_ != '"', 0).! ~~ "\"" )
  def ident[_: P] = P( ident0 ).map(Expr.Ident)
  def ident0[_: P] = P( CharIn("a-zA-Z_") ~~ CharsWhileIn("a-zA-Z0-9_", 0) ).!

  def dict[_: P] = P( "{" ~/ (str0 ~ ":" ~/ expr).rep(0, ",") ~ "}" )
    .map(kvs => Expr.Dict(kvs.toMap))
  def local[_: P] = P( "local" ~/ ident0 ~ "=" ~ expr ~ ";" ~ expr )
    .map(Expr.Local.tupled)
  def func[_: P] = P( "function" ~/ "(" ~ ident0.rep(0, ",") ~ ")" ~ expr )
    .map(Expr.Func.tupled)

  def plus[_: P] = P( "+" ~ prefixExpr )
  def call[_: P] = P( "(" ~/ expr.rep(0, ",") ~ ")" )
}
sealed trait Value
object Value{
  case class Str(s: String) extends Value
  case class Dict(pairs: Map[String, Value]) extends Value
  case class Func(call: Seq[Value] => Value) extends Value
}

def evaluate(expr: Expr, scope: Map[String, Value]): Value = expr match {
  case Expr.Str(s) => Value.Str(s)
  case Expr.Dict(kvs) => Value.Dict(kvs.map{case (k, v) => (k, evaluate(v, scope))})
  case Expr.Plus(left, right) =>
    val Value.Str(leftStr) = evaluate(left, scope)
    val Value.Str(rightStr) = evaluate(right, scope)
    Value.Str(leftStr + rightStr)
  case Expr.Local(name, assigned, body) =>
    val assignedValue = evaluate(assigned, scope)
    evaluate(body, scope + (name -> assignedValue))
  case Expr.Ident(name) => scope(name)
  case Expr.Call(expr, args) =>
    val Value.Func(call) = evaluate(expr, scope)
    val evaluatedArgs = args.map(evaluate(_, scope))
    call(evaluatedArgs)
  case Expr.Func(argNames, body) =>
    Value.Func(args => evaluate(body, scope ++ argNames.zip(args)))
}

def serialize(v: Value): ujson.Value = v match {
  case Value.Str(s) => ujson.Str(s)
  case Value.Dict(kvs) => ujson.Obj.from(kvs.map{case (k, v) => (k, serialize(v))})
}

def jsonnet(input: String): String = {
  ujson.write(
    serialize(evaluate(fastparse.parse(input, Parser.expr(_)).get.value, Map.empty)),
    indent = 2
  )
}
