# Nacos

[nacos官方文档](https://nacos.io/zh-cn/docs/quick-start.html)

跟着官方文档配置的，但是这里我是在windows的环境下使用的，因此下面只是写了windows情况下如何配置

### 预备环境准备

Nacos 依赖 Java 环境来运行。如果您是从代码开始构建并运行Nacos，还需要为此配置 Maven环境，请确保是在以下版本环境中安装使用:

- 64 bit OS，支持 Linux/Unix/Mac/Windows，推荐选用 Linux/Unix/Mac。
- 64 bit JDK 1.8+；
- Maven 3.2.x+；

## 配置

使用服务，使用nacos服务需要去把nacos的压缩包下载一下： [nacos](https://github.com/alibaba/nacos/releases/tag/2.0.1)
这里推荐使用 nacos2.0.1 稳定版  
下载之后进行解压，然后去bin目录启动起来，这里以单点模式启动，之后需要的话在研究集群模式启动方式
`startup.cmd -m standalone`  
启动之后访问 `http://localhost:8848/nacos` 来访问nacos的控制台

接下里，我们就是需要将我们的服务注册上去了。

我们创建一个服务提供者的项目，项目的依赖如下：

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!--  这个actuator可以用来监控服务的运行状况的  -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>

</dependencies>
```
相比于开发单体应用，我们需要增加一些配置，一个是nacos的配置类，一个是在yml中配置如何向注册中心进行服务注册。
配置类只需要两个配置注解，当然我们也可以吧`@EnableDiscoveryClient`这个注解放在启动类上，这里为了后期定制化配置留下的。

```java
@EnableDiscoveryClient
@Configuration
public class NacosDiscoveryConfiguration {

}
```

配置文件如下：
```yaml
server:
  port: 9001
## 应用名称
spring:
  application:
    name: nacos
  cloud:
    nacos:
      discovery:
        username: nacos
        password: nacos
        # Nacos 服务发现与注册配置，其中子属性 server-addr 指定 Nacos 服务器主机和端口
        server-addr: localhost:8848
```

用户名和密码需要同nacos服务上配置的一样，另外`spring.application.name`这个就是服务的名称，多个服务名称相同的节点会被当作
一个服务来进行管理


## nacos + sentinel
我们可以架构nacos作为一个数据源，将每个方法的限流配置告知

## sentinel dashboard
可以进行二次开发，监控服务负载情况