# Dockerfile for nas-server
# Build with:
# docker build -t nas-server .
# Run with:
# docker run -p 18848:18848 --net docker-br0 --ip 172.172.1.109 --env NAS_TARGET_NODES=http://192.168.1.159:9333,http://192.168.1.159:9334,http://192.168.1.159:9335 --env NAS_HOST=192.168.1.151 --add-host nas.acmedcare.com:172.172.1.109 --add-host nacos.acmedcare.com:172.172.1.3 --add-host trace.acmedcare.com:192.168.1.151 --add-host gateway.acmedcare.com:172.172.1.108 -d -v /tmp/logs:/tmp/logs -v /tmp/logs/nas-server:/nas-server/nas/logs --name nas-server docker.apiacmed.com/library/nas-server:2.1.1.BUILD-SNAPSHOT
#
# --env NAS_TARGET_NODES=http://192.168.1.159:9333,http://192.168.1.159:9334,http://192.168.1.159:9335 --env NAS_HOST=192.168.1.151
#
#
#
#
#
FROM docker.apiacmed.com/env/openjdk-acmed:8-jre-alpine
MAINTAINER Elve.Xu <iskp.me@gmail.com>

ENV VERSION 2.1.1.BUILD-SNAPSHOT

RUN echo "http://mirrors.aliyun.com/alpine/v3.6/main" > /etc/apk/repositories \
    && echo "http://mirrors.aliyun.com/alpine/v3.6/community" >> /etc/apk/repositories \
    && apk update upgrade \
    && apk add --no-cache procps unzip curl bash tzdata \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

ADD nas-server-${VERSION}.zip /nas-server/nas-server-${VERSION}.zip

RUN unzip /nas-server/nas-server-${VERSION}.zip -d /nas-server \
    && rm -rf /nas-server/nas-server-${VERSION}.zip \
    && sed -i '$d' /nas-server/nas/bin/startup.sh \
    && echo "tail -f /dev/null" >> /nas-server/nas/bin/startup.sh

EXPOSE 18848

CMD ["/nas-server/nas/bin/startup.sh"]
