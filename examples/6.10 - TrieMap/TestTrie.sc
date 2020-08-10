import $file.Trie, Trie._

val t = new Trie[Int]()

t.add("mango", 1337)
t.add("mandarin", 31337)
t.add("map", 37)
t.add("man", 7)

assert(pprint.log(t.contains("mango")) == true)

assert(pprint.log(t.contains("mang")) == false)

assert(pprint.log(t.contains("man")) == true)

assert(pprint.log(t.contains("mandarin")) == true)

assert(pprint.log(t.contains("mandarine")) == false)

assert(pprint.log(t.prefixesMatchingString("mangosteen")) == Map("man" -> 7, "mango" -> 1337))

assert(pprint.log(t.stringsMatchingPrefix("man")) == Map("man" -> 7, "mandarin" -> 31337, "mango" -> 1337))

assert(pprint.log(t.stringsMatchingPrefix("ma")) == Map("map" -> 37, "man" -> 7, "mandarin" -> 31337, "mango" -> 1337))

assert(pprint.log(t.stringsMatchingPrefix("map")) == Map("map" -> 37))

assert(pprint.log(t.stringsMatchingPrefix("mand")) == Map("mandarin" -> 31337))

assert(pprint.log(t.stringsMatchingPrefix("mando")) == Map())
