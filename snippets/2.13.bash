$ ./mill myScript.scala
Missing arguments: --my-arg <str> --my-other-arg <int>
Expected Signature: main
  --my-arg <str>
  --my-other-arg <int>

$ ./mill myScript.scala --my-arg "mooo" --my-other-arg 7
hello mooo
14
