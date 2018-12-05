## Acmedcare Nas Exts (扩展)

抽象统一的存储Api, 客户端不修改程序进行无缝切换

### TODO LIST

-----

- [x] 七牛对象存储
- [ ] 阿里云对象存储
- [ ] 金山对象存储


### 如何使用

#### 加载公共依赖

- Maven

```xml
  
<dependency>
    <groupId>com.acmedcare.nas</groupId>
    <artifactId>nas-api</artifactId>
    <version>2.1.0-RC1</version>
</dependency>

<dependency>
    <groupId>com.acmedcare.nas</groupId>
    <artifactId>nas-exts-api</artifactId>
    <version>2.1.0-RC1</version>
</dependency>

```

- Libs

[nas-api]()

[nas-exts-api]()




##### 七牛对象存储

- Maven

```xml
<dependency>
    <groupId>com.acmedcare.nas</groupId>
    <artifactId>nas-exts-qiniu</artifactId>
    <version>2.1.0-RC1</version>
</dependency>
```
- Lib

[nas-exts-qiniu]()

- 配置文件

> JRE环境, 将`qiniu.properties`存在在`resources`目录下

```properties

## qiniu example properties
qiniu.access-key=
qiniu.secret-key=
qiniu.bucket-name=
qiniu.public-url=

```

> Android环境, 将`qiniu.properties`存在在`Asserts`目录下

```properties

## qiniu example properties
qiniu.access-key=
qiniu.secret-key=
qiniu.bucket-name=
qiniu.public-url=

```