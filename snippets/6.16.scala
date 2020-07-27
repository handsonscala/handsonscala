@ val t = new Trie(); t.add("mango"); t.add("mandarin"); t.add("map"); t.add("man")

@ t.prefixesMatchingString0("manible")
res1: Set[Int] = Set(3)

@ t.prefixesMatchingString0("mangosteen")
res2: Set[Int] = Set(3, 5)
