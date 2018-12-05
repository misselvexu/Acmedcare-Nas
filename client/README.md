# Nas客户端


## Java 客户端使用

### Maven 依赖

```xml

<dependency>
    <groupId>com.acmedcare.nas</groupId>
    <artifactId>nas-api</artifactId>
    <version>RELEASE</version>
</dependency>

<dependency>
    <groupId>com.acmedcare.nas</groupId>
    <artifactId>nas-client</artifactId>
    <version>RELEASE</version>
</dependency>

```

### 下载依赖文件

> 2.1.1.BUILD-SNAPSHOT
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

## Http Javascript客户端使用

> 基于标准的 `multipart/form-data` 表单上传方式
----

> Request
```
URL:      http://192.168.1.226:18848/acmedcare-nas/nas/submit
Header:   
    NAS-APPID: WEB-CLIENT-ID
    NAS-APPKEY: WEB-CLIENT-KEY
Method:   POST 
```

> Response

- 成功
状态码: 201
返回值:

```json

{
    "code": 0,   // 0-成功
    "data": {
        "fid": "2,0d11de6664", // 文件唯一标识 
        "fileName": "china-map.jpg",  // 文件名称
        "size": 2441427,        // 文件大小
        "eTag": "b0e1e8f8",       
        "fileUrl": "http://192.168.1.226:18848/acmedcare-nas/nas/2,0d11de6664"  // 文件访问路径
    }
}


```


