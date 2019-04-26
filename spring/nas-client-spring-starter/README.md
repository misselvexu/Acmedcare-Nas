## Nas Client Api Spring Boot Starter

Acmedcare+ Nas Client 与 Spring Boot项目自动化集成配置

### Maven 依赖

```xml

<dependency>
    <groupId>com.acmedcare.nas</groupId>
    <artifactId>spring-boot-starter-nas-client</artifactId>
    <version>2.1.1.BUILD-SNAPSHOT</version>
</dependency>

```


### 配置`application.properties`

```properties

## Nas服务器地址
nas.client.server-addrs=47.97.26.165:18848
nas.client.https=false

## 认证秘钥
nas.client.app-id=id
nas.client.app-key=key

```


### 代码Api

```java

// 注入操作实例对象
@Autowired private NasClient nasClient;


// 功能方法

// 1. 上传
this.nasClient.upload(....)

// 2. 下载
this.nasClient.download(....)

```


### 示例代码

> 参考工程: [nas-client-spring-boot-sample](../../samples/nas-client-spring-boot-sample)
