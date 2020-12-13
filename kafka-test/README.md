Broker端参数
log.dirs 指定了Broker需要使用的若干个文件目录的路径。这个参数是没有默认值，要自己指定
log.dir 这个是单路径，其实直接设置上面参数，上面可以挂载到不同的物理路径

replication.factor 将消息多保存几份，防止消息丢失得主要机制
min.insync.replicas 控制消息至少要被写入到多少个副本算是已提交
    replication.factor > min.insync.replicas。如果两个相等，那么只要有一个副本挂机，整个分区就无法正常工作了。推荐设置成replication.factor=min.insync.replicas+1。

    比起单块磁盘，多块物理磁盘同时读写数据有更高的吞吐量
    能够实现故障的转移，这是Kafka1.1版本新引入的功能。坏掉的磁盘上的数据会自动转移到其他正常的磁盘上，而且Broker还能正常工作
listeners 监听器，就是告诉外部连接者需要通过什么协议访问指定主机名和端口开放的Kafa服务
advertised.listeners 和listeners相比多了个advertised，就是Broker对外发布的监听器
    监听器，是若干个逗号分隔的三元组，每个三元组的格式为<协议名称，主机名，端口>

与Zookeeper相关的参数，Zookeeper负责协调管理保存Kafka集群的所有元数据，比如集群都有哪些Broker在运行、创建了哪些Topic，每个Topic都有多少分区以及这些分区的Leader副本都在那些机器上等信息
zookeeper.connect Zookeeper的集群地址

Topic
auto.create.topics.enable 是否允许自动创建Topic，建议生产环境设置成false。
unclean.leader.election.enable 是否允许Unclean Leader选举。每个分区有多个副本来提供高可用。这些副本种只能有一个副本对外提供服务，即所谓的Leader副本。
如果设置成false，坚决不能让那些落后太多的副本竞选Leader。这些做的后果就是这个分区保存比较多的Leader副本挂了，这个分区就不可用了，就没有Leader了。
如果设置为true，允许那些跑的慢的副本中选一个出来当Leader。这样做的后果是数据有可能就丢失了，因为这些副本保存数据不全。
auto.leader.rebalance.enable 是否允许定期进行Leader选举。
如果设置为true，允许Kafka定期对一些Topic分区进行Leader重选举。LeaderA一直表现得很好，那么有可能一段时间后强行卸任换成LeaderB，换一次Leader代价很高。建议设置成false。

log.retention.{hour|minutes|ms} 控制一条消息数据被保存多长时间。从优先级来说ms设置最高、minutes次之、hour最低
log.retention.bytes 这是指定Broker为消息保存得总磁盘容量大小。默认值-1。
message.max.bytes 控制Broker能够接收得最大消息大小。默认值1000012，不到1KB
-----------------------------------------------------------------------------------------------------------------
消息交付可靠性保障，常见的承认有三种：
最多一次：消息可能会丢失，但绝不可能重复发送
至少一次：消息不会丢失，但有可能重复发送
精确一次：消息不会丢失，也不会被重复发送

当Kafka Producer选择重试，也就是再次发送相同的消息。这就是Kafka默认提供的至少一次可靠性保障原因，不过这会导致消息重复发送。
当kafka Producer禁止重试，这样一来，消息要么写入成功，要么写入失败，但绝不会重复发送，提供最多一次交付保障。

幂等性Producer，可以帮你做消息的重复去重，它只能保证单分区上的幂等性，即一个幂等性能够保证某个主题的一个分区上不出现重复消息，它无法实现多个分区的幂等性。
也只能实现单会话上的幂等性，不能实现跨会话的幂等性。重启了Producer进程之后，这种幂等性保证就丧失了。
    每个新的Producer会分配一个唯一的PID。
    对于每个PID，Producer发送数据的每个<Topic，Partition>都对应一个从0开始单调递增的Sequence Number。
    Broker端缓存中保存了这seq number,对于接收的每条消息，如果其序号比Broker缓存中序号大于1则接收它，否则丢弃。

幂等性Producer配置
    enable.idempotence=true

事务型Producer，能够保证将消息原子性地写入到多个分区中。这批消息要么全部写入成功，要么全部失败。
事务型也不惧怕进程的重启。Producer重启回来后，依然保证它们发送消息的精准一次处理。

事务型Producer配置
    enable.idempotence=true
    transaction.id进行设置
-----------------------------------------------------------------------------------------------------------------
Rebalance---消费组重平衡
    一个Consumer Group下所有的Consumer实例就如何消费订阅主题的所有分区达成共识的过程。在Rebalance过程中，
    所有Consumer实例共同参与，在协调者组件的帮助下，完成订阅主题分区的分配，但是，在整个过程中，所有实例
    都不能消费任何消息，因此它对Consumer的TPS影响很大。

Coordinator--协调者，专门为Consumer Group服务，负责为Group执行Rebalance以及提供位移管理和组成员管理

Consumer Group确定Coordinator所在的Broker的算法
    确定有位移主题的哪个分区来保存该Group数据：partitionId=Math.abs(groupId.hashCode()%offsetsTopicPartitionCount).
    找出该分区Leader副本所在的Broker，该Broker即为对应的Coordinator。

Rebalance发生时机
    组成员数量发生变化
    订阅主题数量发生变化
    订阅主题的分区数发生变化

如果某个Consumer实例不能及时地发送这些心跳请求，Coordinator就会认为该Consumer已经‘死’了，从而将其从Group中移除，
然后开启新一轮Rebalance。Consumer端有个参数叫session.timeout.ms，就是被用来表示此事。默认10秒，如果Coordinator在
10秒内没有收到Group下某Consumer实例的心跳，就会认为整个Consumer实例已经挂了。Consumer还提供了一个允许你控制发送心
跳请求频率参数，就是heartbeat.interval.ms。Consumer还有一个参数，控制Consumer实际消费能力对Rebalance的影响，既
max.poll.interval.ms参数。调用两次pool方法的最大时间间隔。默认值5分钟。

    session.timeout.ms(检测consumer发生崩溃所需的时间) > heartbeat.interval.ms(周期性的向Group Coordinator发送心跳)

-----------------------------------------------------------------------------------------------------------------
监控shell
./kafka-consumer-groups.sh --bootstrap-server 127.0.0.1:9092 --describe --group DemoConsumer
每个分区一行数据，这里只有一个分区

GROUP           TOPIC(主题)           PARTITION(分区)  CURRENT-OFFSET(当前最新消费消息的位移值)  LOG-END-OFFSET(当前最新生产的消息位移值)  LAG(前两者差值)             CONSUMER-ID     HOST            CLIENT-ID
DemoConsumer    test                  0                50                                        60                                        10                          -               -               -

主题分区添加
kafka-topics.sh --bootstrap-server 127.0.0.1:9092 --alter --topic test --partitions 2
主题查看
kafka-topics.sh --bootstrap-server 127.0.0.1:9092 --describe  --topic test

主题创建
kafka-topics.sh --bootstrap-server 127.0.0.1:9092 --create --replication-factor 2 --partitions 1 --topic test2
主题删除
kafka-topics.sh --bootstrap-server 127.0.0.1:9092 --delete --topic test2

