
for some reason confluent is failing to start on my mac, which previously gave me no issue
  * how do i debug the services when running via confluent?  There is no link from [Step 6](https://docs.confluent.io/current/quickstart/ce-quickstart.html#ce-quickstart) to any debugging tips for this so i'm going to investigate.
  * experience tells me port conflicts are issues for services.  I happen to know the zk default port, so i'll check that, but this could be helpful to a newbie
  * Indeed I have a zookeeper running somewhere: Could CLI have told me this somehow?
```
➜ telnet localhost 2181
Trying ::1...
Connected to localhost.
Escape character is '^]'.
```
	* There is a zookeeper running out of my confluent folder, but `confluent status` doesn't report it:
```
➜ ps aux | grep zookeeper
rspurgeon        75121   0.0  0.1  6374476  13516 s025  S    12Apr19   2:40.80 /Library/Java/JavaVirtualMachines/jdk1.8.0_192.jdk/Contents/Home/bin/java -Xmx512M -Xms512M -server -XX:+UseG1GC -XX:MaxGCPauseMillis=20 -XX:InitiatingHeapOccupancyPercent=35 -XX:+ExplicitGCInvokesConcurrent -Djava.awt.headless=true -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dkafka.logs.dir=/var/folders/0n/bl4d59lx55lc5200jw7s5mgr2nfcjm/T/confluent.NvDJFgRh/zookeeper/logs -Dlog4j.configuration=file:/Users/rspurgeon/software/confluent/current/bin/../etc/kafka/log4j.properties -cp /Users/rspurgeon/software/confluent/current/bin/../share/java/kafka/*:/Users/rspurgeon/software/confluent/current/bin/../share/java/confluent-support-metrics/*:/usr/share/java/confluent-support-metrics/* org.apache.zookeeper.server.quorum.QuorumPeerMain /var/folders/0n/bl4d59lx55lc5200jw7s5mgr2nfcjm/T/confluent.NvDJFgRh/zookeeper/zookeeper.properties
```
```
➜ confluent status
This CLI is intended for development only, not for production
https://docs.confluent.io/current/cli/index.html

control-center is [DOWN]
ksql-server is [DOWN]
connect is [DOWN]
kafka-rest is [DOWN]
schema-registry is [DOWN]
kafka is [DOWN]
zookeeper is [DOWN]
``` 
* Digging deeper, there are multiple cp services running on my machine that seem to have become 'detached' from the `confluent` cli.

* I've tried repeated `confluent destroy`, it does nothing with the existing confluent background services

* To move forward, i'm going to manually kill all confluent services by looking at:
`ps aux | grep '/Users/rspurgeon/software/confluent'` and then doing `kill -9 <pid>`, this is potentially a dealbreaker for a new evaluator

* How did the services get disconnected from the Confluent CLI state?  no idea...

* After manually killing everything, `confluent start` worked as expected

### Possible ideas for debugging
* The CLI knows that ZK has failed, but only reports `failed`, could the CLI surface whatever it knows about the failure to the cli user to support faster troubleshooting? 
