# Gateway

目前网关的选型大概有一下三种：

1. Zuul 1.0(servlet)/Zuul 2.0(引入netty 支持异步)
2. Spring cloud gateway :基于Netty实现
3. Nginx + lua(Openresty / Kong)：性能最高

如果来自客户端的流量访问很大，可以设置两层网关

- 第一层Nginx集群进行控流，屏蔽爬虫或者是非法请求
- 第二层业务网关：gateway + jwt
    1. 进行限流，限制单点网关自身流量和服务节点的流量：可以使用令牌桶算法，计数器算法、 sentinel自带的限流方式：基于线程的限流和基于平滑时间窗口的限流
    2. 日志 打印日志
    3. jwt在网关层进行权限鉴定
  

## 使用

断言： predicates 关于路由时如何匹配请求用的  
Path断言：  http://chenzifeng.com/oder   根据oder匹配到对应的服务节点上去  
  predicates:  
    - Path=/oder

Query断言： 参数名可以写正则，也可以直接写参数名  
 predicates:  
    - Query=foo,ba

Method断言：  
  predicates:  
    - Method=get

Host断言：  
  predicates:
    - Host= chenzifeng.com
 
Cookie断言：  
  predicates:
    - Cookie=name,czf
  
多个断言可以一起使用  





