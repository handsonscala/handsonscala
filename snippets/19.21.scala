@ def parser[_: P] = P( prefix ~ ws ~ suffix ).map{
    case ("hello", spaces, place) => new Phrase(true, place)
    case ("goodbye", spaces, place) => new Phrase(false, place)
  }
cmd3.sc:2: constructor cannot be instantiated to expected type;
 found   : (T1, T2, T3)
 required: (String, String)
  case ("hello", spaces, place) => new Phrase(true, place)
       ^
Compilation Failed
