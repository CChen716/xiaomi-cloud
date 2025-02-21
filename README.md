已更新手写RPC框架

一、概述

RPC框架主要作用是服务于分布式系统中的远程调用，可以使不同的主机上的服务调用就像在本地调用一样，也就是使得远程调用变得更加高效和可靠，简化网络通信及分布式系统的开发。并且作为框架而言，其又需要尽可能的优化步骤方便开发人员使用。
![image](https://github.com/user-attachments/assets/6ff98b54-d18a-4c57-bb6c-b9533d5a3321)


二、框架特性（主要）

搭配远程注册中心（Nacos）
反射与注解驱动
负载均衡、容错机制、重试机制等
自定义协议
SpringBoot Stater开发
多种序列化方式

三、基本用法

SpringBoot项目用法：

配置文件项：在resources目录下的application.yml文件中读取对应的配置项，配置前缀为rpc

rpc:

  serverPort: rpc服务器的端口-必填
  
  nacosAddr: nacos的地址-必填
  
  packName: 远程接口的包名-服务提供端必填
  
  serverHost: 服务的ip-必填
  
  mock: 是否开启mock服务 true/false -选填
  
  serialize： 序列化器选择：json/kryo/hessian 不填默认为hessian -选填（两端统一）
  
  retryStrategy：重试策略选择：fixedInterval/no  默认为fixedInterval -选填
  
  tolerantStrategy：容错策略选择：failFast/failSafe 默认为failFast -选填

  dataId：nacos配置文件dataID -选填
  
  group：nacos配置文件group -选填
  


关键注解

● @EnableRpc：在提供者中使用，申明为rpc接口。

● @RpcProvider：在提供者中使用，申明为rpc服务提供端。

● @RpcSubscribe:在消费者启动类中使用，申明订阅了rpc服务。

● @RpcReference:在消费端代理对象上使用，表示为rpc代理对象。


服务提供端使用相关：

@EableRpc:

在服务提供端的方法上使用，表示这个方法为rpc的远程调用方法。

![image](https://github.com/user-attachments/assets/cc4e730b-795b-416f-bb33-9ea795a949c3)

@RpcProvider：

在服务端的SpringBoot启动类上使用该注解，表示为RPC的服务提供端并启动rpc服务器。

![image](https://github.com/user-attachments/assets/bb39fb9f-7832-4e44-8c06-71e77ef29e1a)


消费端使用相关：

首先消费端远程调用服务的前提肯定是需要提前知道调用的方法名以及各种参数等等，所以这里建议是消费端引入一个公共的实体类模块，这个实体类模块就是需要调用的服务提供端的接口。然后只需要该接口的方法名和参数等信息即可。



@RpcReference:
在消费端的业务类中使用该注解表示为远程调用的代理对象，就像@Autowired注解类似。rpc框架会处理该注解的对象。
![image](https://github.com/user-attachments/assets/4c249cdf-53cc-4d36-95f6-46e1c456cecd)


消费端进行调用：
		 

@RpcSubscribe:
在消费端的启动类上使用，标识为rpc订阅端，将进行消费端的一些相关配置拉取以及注解扫描工作。

![image](https://github.com/user-attachments/assets/59e9cd69-9c6e-460b-af9d-49a19fe18aea)

如果是非SpringBoot项目，消费端启动api:ConsumerBootstrap.init();
服务提供端启动api:ProviderBootstrap.init();
分别执行对应的方法即可。

案例测试：
首先将两个服务正常启动，包括nacos。然后查看Nacos的控制台页面，可以看到生成的配置文件信息，这个配置文件保存着rpc相关接口信息，而这些信息是方便消费端的一些处理和调用。

![image](https://github.com/user-attachments/assets/c27f9791-dbca-476b-874f-d117188754a2)
![image](https://github.com/user-attachments/assets/2761fba6-8670-4b9c-9e8c-41a91b0f335c)


启动两个服务后 控制台也会打印一些相关信息。


![image](https://github.com/user-attachments/assets/98105a97-1cf2-47c2-97c1-8dd57e466750)

![image](https://github.com/user-attachments/assets/05379dfe-b233-44da-8383-a373d737c070)


启动后使用调用工具或者测试类进行调用测试，这里使用postman工具进行调用测试

调用成功后查看两个服务的打印信息。
消费端打印：
![image](https://github.com/user-attachments/assets/2157d11e-b059-4fb7-b6d0-c6cdf7bb8b86)

服务端打印：



到此表示整个框架流程基本ok
