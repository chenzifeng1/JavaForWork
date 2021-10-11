# k8s

## 介绍
1.  自动化容器部署与复制
2.  随时扩展与收缩容器规模
3. 容器分组Group，并提供容器间的负载均衡
4. 实时监控，即故障监控，自动替换

## 基本概念

- kubernetes master：主服务器，整个集群的管理者，通过主服务器想其他服务器发送自动部署，自动发布等职能
  所有来自外界的数据包，都会有Kubernetes master来接收并分配给其他Node。
  一般情况下，Kubernetes master单独部署在一台物理机上 
- Pod：k8s进行控制的最小单元。
  1. Pod是“容器”的容器，一个Pod可以包含多个Container
  2. Pod是k8s部署的最小单元，一个Pod就是一个进程。
  3. Pod内部容器网络互通，每个Pod都有独立的虚拟Ip
  4. Pod都是部署完整的应用或者模块  

    其中，Pause容器是Pod必备基础容器，Pause有两个职能，
  一个是提供共享的网络空间，另一个是提供共享的Volume挂载数据卷 
- Container
- Label
- Replication
- Service
- Node：
 