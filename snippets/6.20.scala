@ val t = new Trie(); t.add("mango"); t.add("mandarin"); t.add("map"); t.add("man")

@ t.stringsMatchingPrefix("man")
res1: Set[String] = Set("man", "mandarin", "mango")

@ t.stringsMatchingPrefix("ma")
res2: Set[String] = Set("map", "man", "mandarin", "mango")

@ t.stringsMatchingPrefix("map")
res3: Set[String] = Set("map")

@ t.stringsMatchingPrefix("mand")
res4: Set[String] = Set("mandarin")

@ t.stringsMatchingPrefix("mando")
res5: Set[String] = Set()
