#!/bin/sh

pid=`ps ax | grep -i 'nas' |grep java | grep -v grep | awk '{print $1}'`
if [ -z "$pid" ] ; then
        echo "No nasServer running."
        exit -1;
fi

echo "The nasServer(${pid}) is running..."

kill ${pid}

echo "Send shutdown request to nasServer(${pid}) OK"