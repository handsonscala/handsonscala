//| mvnDeps:
//| - com.lihaoyi::fastparse:3.1.1

enum Expr:
  case Str(s: String)
  case Ident(name: String)
  case Plus(left: Expr, right: Expr)
  case Dict(pairs: Map[String, Expr])
  case Local(name: String, assigned: Expr, body: Expr)
  case Func(argNames: Seq[String], body: Expr)
  case Call(expr: Expr, args: Seq[Expr])

object Parser:
  import fastparse.*, MultiLineWhitespace.*
  def expr[T: P]: P[Expr] = P( prefixExpr ~ plus.rep ).map:
    case (e, items) => items.foldLeft(e)(Expr.Plus(_, _))
  
  def prefixExpr[T: P]: P[Expr] = P( callExpr ~ call.rep ).map:
    case (e, items) => items.foldLeft(e)(Expr.Call(_, _))
  
  def callExpr[T: P] = P( str | dict | local | func | ident )

  def str[T: P] = P( str0 ).map(Expr.Str(_))
  def str0[T: P] = P( "\"" ~~/ CharsWhile(_ != '"', 0).! ~~ "\"" )
  def ident[T: P] = P( ident0 ).map(Expr.Ident(_))
  def ident0[T: P] = P( CharIn("a-zA-Z_") ~~ CharsWhileIn("a-zA-Z0-9_", 0) ).!

  def dict[T: P] = P( "{" ~/ (str0 ~ ":" ~/ expr).rep(0, ",") ~ "}" )
    .map(kvs => Expr.Dict(kvs.toMap))
  
  def local[T: P] = P( "local" ~/ ident0 ~ "=" ~ expr ~ ";" ~ expr ).map(Expr.Local(_, _, _))
  def func[T: P] = P( "function" ~/ "(" ~ ident0.rep(0, ",") ~ ")" ~ expr ).map(Expr.Func(_, _))

  def plus[T: P] = P( "+" ~ prefixExpr )
  def call[T: P] = P( "(" ~/ expr.rep(0, ",") ~ ")" )

enum Value:
  case Str(s: String)
  case Dict(pairs: Map[String, Value])
  case Func(call: Seq[Value] => Value)

def evaluate(expr: Expr, scope: Map[String, Value]): Value = expr match
  case Expr.Str(s) => Value.Str(s)
  case Expr.Dict(kvs) => Value.Dict(kvs.map((k, v) => (k, evaluate(v, scope))))
  case Expr.Plus(left, right) =>
    val Value.Str(leftStr) = evaluate(left, scope).runtimeChecked
    val Value.Str(rightStr) = evaluate(right, scope).runtimeChecked
    Value.Str(leftStr + rightStr)
  
  case Expr.Local(name, assigned, body) =>
    val assignedValue = evaluate(assigned, scope)
    evaluate(body, scope + (name -> assignedValue))
  
  case Expr.Ident(name) => scope(name)
  case Expr.Call(expr, args) =>
    val Value.Func(call) = evaluate(expr, scope).runtimeChecked
    val evaluatedArgs = args.map(evaluate(_, scope))
    call(evaluatedArgs)
  
  case Expr.Func(argNames, body) =>
    Value.Func(args => evaluate(body, scope ++ argNames.zip(args)))

def serialize(v: Value): ujson.Value = v.runtimeChecked match
  case Value.Str(s) => ujson.Str(s)
  case Value.Dict(kvs) => ujson.Obj.from(kvs.map((k, v) => (k, serialize(v))))

def jsonnet(input: String): String =
  ujson.write(
    serialize(evaluate(fastparse.parse(input, Parser.expr(using _)).get.value, Map.empty)),
    indent = 2
  )
