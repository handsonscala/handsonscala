//| moduleDeps: [Exprs.scala]
//| mvnDeps:
//| - com.lihaoyi::fastparse:3.1.1
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
