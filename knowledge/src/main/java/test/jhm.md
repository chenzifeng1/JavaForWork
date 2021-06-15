# JMH (Java Microbenchmark Harness) 代码微基准测试的工具套件

主要是基于方法层面的基准测试，精度可以达到纳秒级。该工具是由 Oracle 内部实现 JIT 的大牛们编写的，
他们应该比任何人都了解 JIT 以及 JVM 对于基准测试的影响。当你定位到热点方法，希望进一步优化方法性
能的时候，就可以使用 JMH 对优化的结果进行量化的分析。  
JMH 比较典型的应用场景如下：
1. 想准确地知道某个方法需要执行多长时间，执行时间和输入之间的相关性 
2. 对比接口不同实现在给定条件下的吞吐量
3. 查看多少百分比的请求在多长时间内完成

## 使用
 - JMH是jdk9自带的，如果是jdk9之前的版本需要加入以下依赖:
```xml
<dependency>
    <groupId>org.openjdk.jmh</groupId>
    <artifactId>jmh-core</artifactId>
    <version>1.23</version>
</dependency>

<dependency>
    <groupId>org.openjdk.jmh</groupId>
    <artifactId>jmh-generator-annprocess</artifactId>
    <version>1.23</version>
</dependency>
```

- 插件： jmh
- 注解：Setting-> Build、-> Compiler -> Annotation Processors 勾选Enable Annotation Processors


