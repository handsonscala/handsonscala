//| moduleDeps: [Parse.scala]
import Expr.*
import fastparse.*

def main() =
  assert(
    fastparse.parse("""{"a": "A", "b": "bee"}""", Parser.expr(using _)) ==
    Parsed.Success(Dict(Map("a" -> Str("A"), "b" -> Str("bee"))), 22)
  )

  assert(
    fastparse.parse("""f()(a) + g(b, c)""", Parser.expr(using _)) ==
    Parsed.Success(
      Plus(
        Call(Call(Ident("f"), List()), List(Ident("a"))),
        Call(Ident("g"), List(Ident("b"), Ident("c")))
      ),
      16
    )
  )

  val input = """local thing = "kay"; {"f": function(a) a + a, "nested": {"k": v}}"""

  assert(
    fastparse.parse(input, Parser.expr(using _)) ==
    Parsed.Success(
      Local(
        "thing",
        Str("kay"),
        Dict(
          Map(
            "f" -> Func(List("a"), Plus(Ident("a"), Ident("a"))),
            "nested" -> Dict(Map("k" -> Ident("v")))
          )
        )
      ),
      65
    )
  )
