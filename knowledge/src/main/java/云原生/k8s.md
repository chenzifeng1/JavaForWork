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
- Label ：说明性的标签，相当于每个pod的别名。k8s主节点通过pod名称对其进行操作
- Replication Controller（复制控制器）：存在于主节点上，对pod数量进行监控。当k8s中的指定label的pod少于预期数量，
  Replication Controller就会复制对应的pod到宿主机。同时Replication Controller还会监控Pod是否有响应，如果没有响应了，
  Replication Controller就会将该pod踢出。
- Service： 跨主机、跨容器战之间的网络通信。逻辑上将容器进行分组，使得同一个service内部的容器无障碍通信
- Node：

## 实现
k8s的Node中，存在三个程序：
1. docker
2. kubelet
3. kube-proxy： 实现跨容器的网络通信，容器A消息发送个kube-proxy，然后kube-proxy将消息转发到对应的容器内部。

## 安装
### 安装方式
1. 使用kubeadmin通过离线镜像安装（推荐）
2. 使用阿里公有云平台k8s（不免费）
3. 通过yum官方仓库安装，版本较老
4. 二进制安装包形式进行安装，kubeasz（github开源项目，有风险）
 