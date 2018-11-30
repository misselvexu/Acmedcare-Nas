#!/usr/bin/env bash

# ./weed upload -master=localhost:9333 /Users/ive/git-acmedcare/README.md /Users/ive/git-acmedcare/Acmedcare-DICOM/README.md

ff(){
  list=`find $1 \( ! -regex '.*/\..*' \) -type f -regex '.*\.'$2`

  for i in $list
    do
    rdn=`head -200 /dev/urandom | cksum | cut -f1 -d" "`
    temp=${i%/*}'/'${rdn}'.dcm'
    mv ${i} ${temp}
    echo ${temp}
  done
}

#
# Example:
#   sh pzip.sh BASE_DIR ZIP_FILE UNZIP_DIR
#
# pzip.sh /Users/ive/git-acmedcare/Acmedcare-DICOM/tmp-files /Users/ive/git-acmedcare/Acmedcare-DICOM/tmp-files/DICOMs.zip /Users/ive/git-acmedcare/Acmedcare-DICOM/tmp-files/tmp
#

# parse zip base dir
BASE_DIR=`echo "$1" | grep -o "[^ ]\+\( \+[^ ]\+\)*"`
ZIP_FILE=`echo "$2" | grep -o "[^ ]\+\( \+[^ ]\+\)*"`
# parse unzip tmp local.disk.dir
UNZIP_DIR=`echo "$3" | grep -o "[^ ]\+\( \+[^ ]\+\)*"`

if [ ! -n "${BASE_DIR}" -o ! -n "${ZIP_FILE}" -o ! -n "${UNZIP_DIR}" ]
then
    echo "Please Defined BASE_DIR / ZIP_FILE /UNZIP_DIR."
    exit -1
fi

BASE_DIR_PATH_END=`echo ${BASE_DIR: -1}`
if [ "${BASE_DIR_PATH_END}" == "/" ];
then
    BASE_DIR=${BASE_DIR%*/}
fi

# check unzip dir
if [ -d "${UNZIP_DIR}" ]; then
  rm -rf ${UNZIP_DIR} >/dev/null 2>&1
fi

mkdir -p ${UNZIP_DIR} >/dev/null 2>&1

if [ $? -eq 0 ]; then
  # unzip
  unzip -oq ${ZIP_FILE} -d ${UNZIP_DIR} >/dev/null 2>&1

  if [ $? -eq 0 ]; then
    # list all dcm files
    ff "$UNZIP_DIR" "dcm"
  else
    exit -10001
  fi
else
  exit -10000
fi
