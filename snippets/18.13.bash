$ mkdir src/hello

$ echo "Hello World" > src/hello/world.txt

$ echo "I am Cow" > src/moo

$ find src -type f
src/moo
src/hello/world.txt

$ find dest -type f
dest/moo
dest/hello/world.txt

$ cat dest/hello/world.txt
Hello World

$ cat dest/moo
I am Cow
