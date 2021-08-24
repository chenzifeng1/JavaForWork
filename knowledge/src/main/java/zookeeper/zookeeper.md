# zookeeper

## 理论

### CAP理论
- C consistency 一致性:  
- A Availability 可用性:  
- P Partition Tolerance 分区容错性:

基于P是被必须满足的，所以只能选择AP或者CP，zookeeper追求的是CP(一致性+分区容错性)，Eureka追求的是AP(可用性+分区容错性)。

### Base理论