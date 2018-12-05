## Nas客户端使用

### Maven 依赖

```xml

<dependency>
    <groupId>com.acmedcare.nas</groupId>
    <artifactId>nas-api</artifactId>
    <version>2.1.0-RC1</version>
</dependency>

<dependency>
    <groupId>com.acmedcare.nas</groupId>
    <artifactId>nas-client</artifactId>
    <version>2.1.0-RC1</version>
</dependency>

```

### 下载依赖文件

> 2.1.0-RC1
------
- [Nas Api](http://115.29.47.72:8082/acmedback/Acmedcare-NewIM/uploads/f88083eafe55aac197b4416a94475f17/nas-api-2.1.0.BUILD-20181202.063950-1.jar)
- [Nas Client](http://115.29.47.72:8082/acmedback/Acmedcare-NewIM/uploads/6f5561a643f2466ffa8181922b281734/nas-client-2.1.0.BUILD-20181202.063952-1.jar)



### 初始化

```java

// 构建Nas配置
NasProperties nasProperties = new NasProperties();
nasProperties.setServerAddrs(Lists.newArrayList("192.168.1.226:18848"));
nasProperties.setHttps(false);
nasProperties.setAppId("nas-app-id"); // 没有就不填写
nasProperties.setAppKey("nas-app-key"); // 没有就不填写


// 构建操作客户端(建议单例)
NasClient nasClient = NasClientFactory.createNewNasClient(nasProperties);


// 功能演示
// 上传
nasClient.upload(....)

// 下载
nasClient.download(.....)


```

### 备注

- 上传文件成功返回值样式
----------------
JSON & Bean Defined
```json

{
	"fid": "1,038c935677",  //  文件唯一标识符
	"publicUrl": "http://192.168.1.226:18848/acmedcare-nas/nas/1,038c935677", // 文件分布式访问路径
	"responseCode": "UPLOAD_OK"
}

``` 