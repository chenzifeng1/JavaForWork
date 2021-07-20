# RabbitMQ

RabbitMQ实现了AMQP(Advance Message Queue Protocol)，在协议中定义了一些接口:

- Broker: 接收和分发消息的应用，我们在介绍消息中间件的时候所说的消息系统就是Message Broker。
- Virtual host: 类似namespace，为每个租户可以创建专属的exchange和queue
- Connection: 消息的publisher和consumer与broker的连接，通常断开只会发生在client端
- Channel: Channel是在connection内部建立的逻辑连接，如果应用程序支持多线程，通常每个thread创建单独的channel进行通讯，AMQP method包含了channel id帮助客户端和message broker识别channel，所以channel之间是完全隔离的。Channel作为轻量级的Connection极大减少了操作系统建立TCP connection的开销。
- Exchange: 根据分发规则，匹配查询表中的routing key，分发消息到queue中去



RabbitMQ限制并发的瓶颈：

> 一个是顺序读写 零拷贝吧 另一个kafka的数据结构比rabbitmq基于erlang的m啥的数据库要简单很多 另外kafka是自己写主要的通信模型  而rabbitmq躺在erlang身上。。。而一般躺在另一个方案身上  类似一种 从胶水语言到 实际执行层的转换吧。。。

> 另外还有一个。。。。kafka的消息存储方式 和 rabbitmq的方式 有很大区别   kafka的方式是offset指针那样的  所谓消费确认 仅仅是指针标记往后移动  而amqp的rabbitmq是正儿八经的消费确认就删除 阅过即焚

> 数据结构上  rabbitmq有unacked状态。。。。。kafka就没有这回事  因为   unacked 是每条消息 都有多个状态的 而kafka 的消息确认 是一个分区 一个groupid 存一个指针位 offset 就行了吧
