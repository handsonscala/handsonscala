> val t = Trie(); t.add("mango"); t.add("mandarin"); t.add("map"); t.add("man")

> t.stringsMatchingPrefix("man")
res14: Set[String] = Set("man", "mandarin", "mango")

> t.stringsMatchingPrefix("ma")
res15: Set[String] = Set("map", "man", "mandarin", "mango")

> t.stringsMatchingPrefix("map")
res16: Set[String] = Set("map")

> t.stringsMatchingPrefix("mand")
res17: Set[String] = Set("mandarin")

> t.stringsMatchingPrefix("mando")
res18: Set[String] = Set()
