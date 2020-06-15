import $file.Trie, Trie._

val t = new ImmutableTrie(Seq("mango", "mandarin", "map", "man"))

assert(pprint.log(t.contains("mango")) == true)

assert(pprint.log(t.contains("mang")) == false)

assert(pprint.log(t.contains("man")) == true)

assert(pprint.log(t.contains("mandarin")) == true)

assert(pprint.log(t.contains("mandarine")) == false)

assert(pprint.log(t.prefixesMatchingString("mangosteen")) == Set("man", "mango"))

assert(pprint.log(t.stringsMatchingPrefix("man")) == Set("man", "mandarin", "mango"))

assert(pprint.log(t.stringsMatchingPrefix("ma")) == Set("map", "man", "mandarin", "mango"))

assert(pprint.log(t.stringsMatchingPrefix("map")) == Set("map"))

assert(pprint.log(t.stringsMatchingPrefix("mand")) == Set("mandarin"))

assert(pprint.log(t.stringsMatchingPrefix("mando")) == Set())
