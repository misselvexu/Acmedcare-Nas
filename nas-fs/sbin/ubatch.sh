#!/usr/bin/env bash

#
#  /Users/ive/git-acmedcare/Acmedcare-DICOM/dicom-fs/bin/Darwin/weed upload -master=localhost:9333 /Users/ive/git-acmedcare/README.md /Users/ive/git-acmedcare/Acmedcare-DICOM/README.md
#
#
# ./ubatch.sh /Users/ive/git-acmedcare/Acmedcare-DICOM/dicom-fs localhost:9333 /Users/ive/git-acmedcare/README.md /Users/ive/git-acmedcare/Acmedcare-DICOM/README.md
#
#

OS_NAME=$(uname)
WEED_BASE_DIR=`echo "$1" | grep -o "[^ ]\+\( \+[^ ]\+\)*"`
SERVER_ADDRESS=`echo "$2" | grep -o "[^ ]\+\( \+[^ ]\+\)*"`
S_FILES=`echo "${@:3}" | grep -o "[^ ]\+\( \+[^ ]\+\)*"`

${WEED_BASE_DIR}/bin/${OS_NAME}/weed upload -master=${SERVER_ADDRESS} ${S_FILES}