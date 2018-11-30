#!/usr/bin/env bash

OS_NAME=$(uname)
echo '系统:'${OS_NAME}

case $1 in
slave)

  BASE_DIR=`echo "$2" | grep -o "[^ ]\+\( \+[^ ]\+\)*"`
  SERVER_ADDRESS=`echo "$3" | grep -o "[^ ]\+\( \+[^ ]\+\)*"`
  BASE_DIR_PATH_END=`echo ${BASE_DIR: -1}`
  if [ "${BASE_DIR_PATH_END}" == "/" ];
  then
      BASE_DIR=${BASE_DIR%*/}
  fi

  if [ ! -n "${SERVER_ADDRESS}" ]
  then
      SERVER_ADDRESS="${SERVER_ADDRESS}"
  fi

  echo '跟路径:'${BASE_DIR}
  echo '服务器地址:'${SERVER_ADDRESS}
  echo '启动文件服务器主服务(s) ...'
	echo '启动主服务节点一, 端口:9333'
	NODE_1_DIR=${BASE_DIR}/masters/master-1
	if [ ! -d "${NODE_1_DIR}" ]; then
    mkdir -p ${NODE_1_DIR} >/dev/null 2>&1
  fi
  nohup ./${OS_NAME}/weed master -port=9333 -mdir=${NODE_1_DIR} -defaultReplication="002" -peers=${SERVER_ADDRESS}:9333,${SERVER_ADDRESS}:9334,${SERVER_ADDRESS}:9335 -ip=${SERVER_ADDRESS} >> local-master-server-node1.log 2>&1 &

  sleep 2

  echo '启动主服务节点二, 端口:9334'
  NODE_2_DIR=${BASE_DIR}/masters/master-2
	if [ ! -d "${NODE_2_DIR}" ]; then
    mkdir -p ${NODE_2_DIR} >/dev/null 2>&1
  fi
  nohup ./${OS_NAME}/weed master -port=9334 -mdir=${NODE_2_DIR} -defaultReplication="002" -peers=${SERVER_ADDRESS}:9333,${SERVER_ADDRESS}:9334,${SERVER_ADDRESS}:9335 -ip=${SERVER_ADDRESS} >> local-master-server-node2.log 2>&1 &

  sleep 2

  echo '启动主服务节点三, 端口:9335'
  NODE_3_DIR=${BASE_DIR}/masters/master-3
	if [ ! -d "${NODE_3_DIR}" ]; then
    mkdir -p ${NODE_3_DIR} >/dev/null 2>&1
  fi
  nohup ./${OS_NAME}/weed master -port=9335 -mdir=${NODE_3_DIR} -defaultReplication="002" -peers=${SERVER_ADDRESS}:9333,${SERVER_ADDRESS}:9334,${SERVER_ADDRESS}:9335 -ip=${SERVER_ADDRESS} >> local-master-server-node3.log 2>&1 &

  sleep 2

  echo '启动存储节点一, 端口:9080'

  V_NODE_1_DIR=${BASE_DIR}/volumes/volume-1
	if [ ! -d "${V_NODE_1_DIR}" ]; then
    mkdir -p ${V_NODE_1_DIR} >/dev/null 2>&1
  fi
  nohup ./${OS_NAME}/weed volume -dir=${V_NODE_1_DIR} -max=100 -mserver="${SERVER_ADDRESS}:9333,${SERVER_ADDRESS}:9334,${SERVER_ADDRESS}:9335" -port=9080 -ip=${SERVER_ADDRESS} >> local-volume-server-node1.log 2>&1 &

  sleep 1

  echo '启动存储节点二, 端口:9081'
  V_NODE_2_DIR=${BASE_DIR}/volumes/volume-2
	if [ ! -d "${V_NODE_2_DIR}" ]; then
    mkdir -p ${V_NODE_2_DIR} >/dev/null 2>&1
  fi
  nohup ./${OS_NAME}/weed volume -dir=${V_NODE_2_DIR} -max=100 -mserver="${SERVER_ADDRESS}:9333,${SERVER_ADDRESS}:9334,${SERVER_ADDRESS}:9335" -port=9081 -ip=${SERVER_ADDRESS} >> local-volume-server-node2.log 2>&1 &

  sleep 1

  echo '启动存储节点三, 端口:9082'
  V_NODE_3_DIR=${BASE_DIR}/volumes/volume-3
	if [ ! -d "${V_NODE_3_DIR}" ]; then
    mkdir -p ${V_NODE_3_DIR} >/dev/null 2>&1
  fi
  nohup ./${OS_NAME}/weed volume -dir=${V_NODE_3_DIR} -max=100 -mserver="${SERVER_ADDRESS}:9333,${SERVER_ADDRESS}:9334,${SERVER_ADDRESS}:9335" -port=9082 -ip=${SERVER_ADDRESS} >> local-volume-server-node3.log 2>&1 &

  echo '启动完毕!'

  ps -ef | grep ./${OS_NAME}/weed | grep -v grep

;;
single)

  echo 'Startup Acmedcare+ DICOM Storage Master Server ....'
  nohup ./${OS_NAME}/weed master -port=9333 -mdir=../local.data -peers=${SERVER_ADDRESS}:9333 -ip="${SERVER_ADDRESS}" >> local-master-server.log 2>&1 &

  echo 'Startup Acmedcare+ DICOM Storage Volume Instance ....'
  nohup ./${OS_NAME}/weed volume -dir=../local.data/local.vol/ -max=100 -mserver="${SERVER_ADDRESS}:9333" -port=9080 -ip="${SERVER_ADDRESS}" >> local-volume-server.log 2>&1 &

  echo 'Started!'

  ps -ef | grep ./${OS_NAME}/weed | grep -v grep
;;
*)
   echo "Usage: $0 {slave|single} [slave base dir]" >&2

esac
