#!/usr/bin/env bash

#
# /Users/ive/git-acmedcare/Acmedcare-DICOM/dicom-fs/bin/weed download -server=localhost:9333 4,0c19944311
#
#
#  ./dbatch.sh /Users/ive/git-acmedcare/Acmedcare-DICOM/dicom-fs /Users/ive/git-acmedcare/Acmedcare-DICOM/tmp-files localhost:9333 4,0c19944311 5,0ff21c5617 5,0ff21c5617_1
#
OS_NAME=$(uname)
WEED_BASE_DIR=`echo "$1" | grep -o "[^ ]\+\( \+[^ ]\+\)*"`
DEST_DIR=`echo "$2" | grep -o "[^ ]\+\( \+[^ ]\+\)*"`
SERVER_ADDRESS=`echo "$3" | grep -o "[^ ]\+\( \+[^ ]\+\)*"`
D_DIFS=`echo "${@:4}" | grep -o "[^ ]\+\( \+[^ ]\+\)*"`


# check unzip dir
if [ -d "${DEST_DIR}" ]; then
  rm -rf ${DEST_DIR} >/dev/null 2>&1
fi

mkdir -p ${DEST_DIR} >/dev/null 2>&1


cd ${DEST_DIR}
${WEED_BASE_DIR}/bin/${OS_NAME}/weed download -server=${SERVER_ADDRESS} ${D_DIFS}
