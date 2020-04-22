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



