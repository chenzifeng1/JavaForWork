# MySql 调优
从三个层次了解mysql
1. Client
2. Server： 连接器  
   ->分析器（对sql语句进行语法分析，构成一个AST,抽象语法树）  
   ->优化器（两种优化方式，RBO 基于规则的优化/CBO 基于成本的优化）
   ->执行器（sql语句实际执行的组件）
   mysql8.0之前存在缓存这一层，但是由于命中率不高在8.0版本移除了
3. 存储引擎
## 性能监控
- 使用 `show profile`查询剖析工具，可以指定具体的type
见官网文档[mysql性能](https://dev.mysql.com/doc/refman/8.0/en/performance-schema.html)
- 使用`performance_schema`  