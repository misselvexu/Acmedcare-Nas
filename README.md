## Acmedcare-Nas

Acmedcare+ Distribution Storage System

### Environmental deployment

#### Download

> 2.1.0.BUILD-SNAPSHOT

----

[Acmedcare Nas Server](releases/2.1.0.BUILD-SNAPSHOT/nas-fs-2.1.0.BUILD-SNAPSHOT-assembly.tar.gz)

[Acmedcare Nas Proxy](releases/2.1.0.BUILD-SNAPSHOT/nas-server-2.1.0.BUILD-SNAPSHOT.tar.gz)

#### Startup
----

- Acmedcare Nas Server

> ignore


- Acmedcare Nas Proxy

```bash
  
  # startup
  ./bin/startup.sh -m daemon

  # shutdown
  ./bin/shutdown.sh

```


### Nas Ext Proxy
Nas Proxy for client to use, current implements:

-[x] Acmedcare Nas OSS  (@see guide [README.md](client/README.md))
-[x] QiNiu OSS (@see guide [README.md](nas-exts/README.md))
-[ ] Aliyun OSS


### Nas Endpoint

> coming soon~