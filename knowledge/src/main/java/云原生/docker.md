# docker

Linux 安装 （以Centos为例）：

官方文档[doecker CentOS安装](https://docs.docker.com/engine/install/centos/)

## 安装步骤

1. 卸载掉旧版本的`docker`以及`doceker-engine`

   ```shell 
   sudo yum remove docker \
                     docker-client \
                     docker-client-latest \
                     docker-common \
                     docker-latest \
                     docker-latest-logrotate \
                     docker-logrotate \
                     docker-engine   	
   ```

2. 安装yum工具包`yum-utils`,这个工具包可以帮助我们来更好的安装我们所需要的软件

   ```shell
   sudo yum install yum-utils
   ```

3. 使用`yum-config-manager`来添加docker对应的安装源

   ```shell
   sudo yum-config-manager \
       --add-repo \
       https://download.docker.com/linux/centos/docker-ce.repo
   ```

   这里需要注意的是，`--add-repo` 是设置新的安装源，官网上的安装源默认的是国外docker服务器上的源。我们这里可以使用阿里云的安装源。另外`docker-ce`的后缀`ce`
   表示当前版本是社区开源版，如果是企业收费版则后缀名应该是`ee`。

4. 安装docker，命令比较简单

   ```shell
   yum -y install docker-ce	
   ```

5. 启动docker

   ```shell
   service docker start
   ```

6. 验证docker是否安装成功

   ```shell
   docker version
   ```

   docker会将服务端和客户端同时安装好，默认docker的客户端连接的是本地服务端

7. 验证docker是否能运行,验证方法是从docker服务端拉取一个demo镜像文件。

   ```shell
   docker pull hello-world
   ```

   基于hello-world这个镜像文件创建对应容器

   ```shell
   docker run hello-world
   ```

### 设置阿里云的加速代理

去阿里云中查找`容器镜像服务`->`镜像加速器`

这里配置的时候需要注意docker的安装版本是否符合要求。

   ```shell
   sudo mkdir -p /etc/docker
   sudo tee /etc/docker/daemon.json <<-'EOF'
   {
     "registry-mirrors": ["https://*******.mirror.aliyuncs.com"]
   }
   EOF
   sudo systemctl daemon-reload
   sudo systemctl restart docker
   ```

### Tomcat

Tomcat:lastest 容器内部结构，包括三部分
- Linux(Red Hat4.8.5-28) : Linux对应的版本非常轻量，不会带来太多负担，甚至轻量化到只能支持容器内的其他组件。
  但是正是由于有这个操作系统的存在，对于资源的控制和操作就有了保障。
- jdk(1.8.0_222)
- Appche Tomcat/8.5.46

### 在容器中执行命令：

- docker exec [-it] 容器id 命令
    1. exec 在对应容器中执行命令
    2. -it 以交互式的方式执行命令，即如果我们后期需要在这个基础上对容器执行其他命令  
   实例： docker exec -it ******** /bin/bash
       

    