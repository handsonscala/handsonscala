$ scala --import com.lihaoyi::fastparse:3.1.1 --repl

> import fastparse.*, NoWhitespace.*

> def parser[T: P] = P( "hello" )
