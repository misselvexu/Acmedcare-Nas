#!/usr/bin/env bash

PIDS=`ps -ef | grep 'weed volume' | grep -v grep | awk '{print $2}'`
if [ -z "$PIDS" ]; then
    echo "ERROR: The Weed Volume(s) does not started!"
    exit 1
fi

echo -e "Stopping the Weed Volume(s)  ...\c"
for PID in ${PIDS} ; do
    kill ${PID} > /dev/null 2>&1
done

COUNT=0
while [ ${COUNT} -lt 1 ]; do
    echo -e ".\c"
    sleep 1
    COUNT=1
    for PID in ${PIDS} ; do
        PID_EXIST=`ps -p ${PID} | grep 'weed volume'`
        if [ -n "$PID_EXIST" ]; then
            COUNT=0
            break
        fi
    done
done

echo "OK!"
echo "PID: $PIDS"