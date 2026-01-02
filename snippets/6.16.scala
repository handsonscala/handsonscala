> val t = Trie(); t.add("mango"); t.add("mandarin"); t.add("map"); t.add("man")

> t.prefixesMatchingString0("manible")
res11: Set[Int] = Set(3)

> t.prefixesMatchingString0("mangosteen")
res12: Set[Int] = Set(3, 5)
