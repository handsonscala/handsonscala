> while true do { Thread.sleep(1000); println(1 + 1) } // loop forever
2
2
2
^C
Attempting to interrupt running thread with `Thread.interrupt`
java.lang.InterruptedException: sleep interrupted
  at java.base/java.lang.Thread.sleep0(Native Method)
  at java.base/java.lang.Thread.sleep(Thread.java:509)
  ... 30 elided

>
