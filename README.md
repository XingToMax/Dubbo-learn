> 本篇主要用于记录基于dubbo完成的第一个远程调用程序

### Dubbo介绍

Apache Dubbo 是一款由阿里开源的高性能的Java RPC框架，具有面向接口代理的高性能RPC调用、智能负载均衡、服务自动注册与发现等特性。

Dubbo的基础架构主要包括Provider(服务供应方)、Consumer(服务消费方)、Registry(服务注册与发现的服务中心)等部分，本文主要围绕前三者的搭建展开。

### 环境介绍

+ 服务注册中心 : ZooKeeper
+ Zookeeper搭建使用的系统 : centos6
+ IDE : Intellij IDEA

### Zookeeper环境配置

> Zookeeper是一个开源的分布式协调服务系统，支持实现数据发布/订阅、负载均衡、命名服务、分布式协调/通知等功能，可以将复杂且容易出错的分布式一致性服务封装起来，构成一个高效可靠的原语集，并以一系列简单易用的接口提供给用户使用

这里，根据Dubbo官方的推荐，使用Zookeeper作为服务注册中心，会在Centos(我是通过Vmware创建的)上完成Zookeeper的搭建

```
mkdir -p /usr/local/services/zookeeper
cd /usr/local/services/zookeeper
# 这里的zookeeper版本可以自己选择合适的，注意修改后续包含版本的路径
wget https://archive.apache.org/dist/zookeeper/zookeeper-3.4.9/zookeeper-3.4.9.tar.gz
tar xzf zookeeper-3.4.9.tar.gz
cd zookeeper-3.4.9/conf/
cp zoo_sample.cfg zoo.cfg
vi zoo.cfg
```

设置内容如下

``` 
# The number of milliseconds of each tick

# zookeeper 定义的基准时间间隔，单位：毫秒
tickTime=2000

# The number of ticks that the initial 
# synchronization phase can take
initLimit=10
# The number of ticks that can pass between 
# sending a request and getting an acknowledgement
syncLimit=5
# the directory where the snapshot is stored.
# do not use /tmp for storage, /tmp here is just 
# example sakes.
# dataDir=/tmp/zookeeper

# 数据文件夹
dataDir=/usr/local/services/zookeeper/zookeeper-3.4.9/data

# 日志文件夹
dataLogDir=/usr/local/services/zookeeper/zookeeper-3.4.9/logs

# the port at which the clients will connect
# 客户端访问 zookeeper 的端口号
clientPort=2181

# the maximum number of client connections.
# increase this if you need to handle more clients
#maxClientCnxns=60
#
# Be sure to read the maintenance section of the 
# administrator guide before turning on autopurge.
#
# http://zookeeper.apache.org/doc/current/zookeeperAdmin.html#sc_maintenance
#
# The number of snapshots to retain in dataDir
#autopurge.snapRetainCount=3
# Purge task interval in hours
# Set to "0" to disable auto purge feature
#autopurge.purgeInterval=1
```

保存并退出

```
cd ../bin/
vi /etc/profile
```

文末追加

``` 
#idea - zookeeper-3.4.9 config start - 2017-12-09
export ZOOKEEPER_HOME=/usr/local/services/zookeeper/zookeeper-3.4.9/
export PATH=$ZOOKEEPER_HOME/bin:$PATH
export PATH
#idea - zookeeper-3.4.9 config end - 2017-12-09
```

保存退出

```
# 启动服务
./zkServer.sh start
# 查询状态
./zkServer.sh status
# 关闭服务
./zkServer.sh stop
```

到此，完成Zookeeper的安装

### 创建项目

1. 在IDEA中创建一个maven项目

2. 创建service包

3. 创建HelloService接口

``` java
public interface HelloService {
    /**
     * say hello 
     * @param name user name
     * @return hello, name
     */
    String sayHello(String name);
}
```

### 创建服务供应方

1. 在上述项目下新建一个spring boot的module

2. 配置pom依赖，主要添加dubbo和zookeeper的依赖

``` xml
<dependencies>
    <!-- 添加上述maven项目的依赖，使用定义好的server接口 -->
    <dependency>
        <groupId>org.nuaa.tomax</groupId>
        <artifactId>dlearn</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
     <!-- dubbo 依赖 -->
    <dependency>
        <groupId>io.dubbo.springboot</groupId>
        <artifactId>spring-boot-starter-dubbo</artifactId>
        <version>1.0.0</version>
    </dependency>
     <!-- zookeeper依赖 -->
    <dependency>
        <groupId>org.apache.zookeeper</groupId>
        <artifactId>zookeeper</artifactId>
        <version>3.4.6</version>
        <exclusions>
            <exclusion>
                <groupId>org.slf4j</groupId>
                <artifactId>slf4j-log4j12</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    <!-- spring web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

3. 设置application.properties

``` properties
## 设置启动端口
server.port=8082
## Dubbo 服务提供者配置
spring.dubbo.application.name=provider
## 设置zookeeper地址
spring.dubbo.registry.address=zookeeper://ip:host
spring.dubbo.protocol.name=dubbo
spring.dubbo.protocol.port=20880
## 设置提供service的包
spring.dubbo.scan=org.nuaa.tomax
```

4. 添加service实现类，创建HelloService

``` JAVA
// 这里的service是dubbo包内的service，不是spring 的service注解
@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "hello, " + name;
    }
}
```

5. 运行main函数启动

### 创建服务消费方

1. 在上述项目下新建一个spring boot的module
2. 配置pom依赖，主要添加dubbo和zookeeper的依赖，和供应方依赖相同，故略
3. 设置application.properties

``` properties
server.port=8081
spring.dubbo.application.name=consumer
spring.dubbo.registry.address=zookeeper://ip:host
spring.dubbo.protocol.name=dubbo
spring.dubbo.protocol.port=20880
spring.dubbo.scan=org.nuaa.tomax
```

1. 添加controller，新建HelloController

``` java
@RestController
@RequestMapping
public class HelloController {
    @Reference
    private HelloService helloService;
    
    @GetMapping("/hello/{name}")
    public String hello(@PathVariable(name = "name") String name) {
        return helloService.sayHello(name);
    }
}
```

1. 运行main函数启动
2. 访问http://localhost:8081/hello/tomax，可以看到调用供应方的实现结果

### Demo地址

[Github](https://github.com/XingToMax/Dubbo-learn)

### 小结

这里只是简单去创建一个Hello world的实例，关于Dubbo的复杂应用与其原理还需继续学习。

### 参考文章

[Spring-boot:5分钟整合Dubbo构建分布式服务](https://www.cnblogs.com/jaycekon/p/SpringBootDubbo.html)

[可能是把 ZooKeeper 概念讲的最清楚的一篇文章](https://github.com/Snailclimb/Java_Guide/blob/master/%E4%B8%BB%E6%B5%81%E6%A1%86%E6%9E%B6/ZooKeeper.md)

[Dubbo 总结：关于 Dubbo 的重要知识点](https://github.com/Snailclimb/Java-Guide/blob/master/%E8%AE%A1%E7%AE%97%E6%9C%BA%E7%BD%91%E7%BB%9C%E4%B8%8E%E6%95%B0%E6%8D%AE%E9%80%9A%E4%BF%A1/dubbo.md)

[在 CentOS7 上安装 Zookeeper服务](https://www.cnblogs.com/huangjianping/p/8012580.html)