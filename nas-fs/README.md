## DICOM Distribute Storage File System

### 单机启动

```bash
  # 启动 master 节点
  nohup ./weed -v=1 master -port=9333 -mdir=../../local.data -peers=127.0.0.1:9333 -defaultReplication=002 -ip="127.0.0.1" > local-master-server.log 2>&1 &
  

  # 启动 Volume 节点
  nohup ./weed -v=1 volume -dir=../../local.data/local.vol/ -max=100 -mserver="127.0.0.1:9333" -port=9080 -ip="127.0.0.1" > local-volume-server.log 2>&1 &

```

### 集群启动

```bash
  
  nohup ./weed && \
        -v=3 master && \
        -port=9333 && \
        -mdir=./m1 && \
        -peers=localhost:9333,localhost:9334,localhost:9335 && \ 
        -defaultReplication=100 && \ 
        -ip="127.0.0.1" >> server_master.log &
  
```