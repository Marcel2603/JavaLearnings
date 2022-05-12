#!/usr/bin/env bash

RESOURCE_FOLDER="src/test/resources"

NUMBER_OF_ELEMENTS=$1
ZIP_NAME=$2
FILE_SIZE=$3

TEMP_FOLDER="/tmp/zipping"

pwd=$(pwd)

mkdir -p $TEMP_FOLDER
cd $TEMP_FOLDER
real_numer=$((NUMBER_OF_ELEMENTS / 10))
for i in $(seq 1 $real_numer) ; do
  head -c $FILE_SIZE < /dev/urandom > "file_${i}_1"
  head -c $FILE_SIZE < /dev/urandom > "file_${i}_2"
  head -c $FILE_SIZE < /dev/urandom > "file_${i}_3"
  head -c $FILE_SIZE < /dev/urandom > "file_${i}_4"
  head -c $FILE_SIZE < /dev/urandom > "file_${i}_5"
  head -c $FILE_SIZE < /dev/urandom > "file_${i}_6"
  head -c $FILE_SIZE < /dev/urandom > "file_${i}_7"
  head -c $FILE_SIZE < /dev/urandom > "file_${i}_8"
  head -c $FILE_SIZE < /dev/urandom > "file_${i}_9"
  head -c $FILE_SIZE < /dev/urandom > "file_${i}_10"
done

zip -9 -q $ZIP_NAME "file_"*

cd $pwd
cp ${TEMP_FOLDER}/${ZIP_NAME} ${RESOURCE_FOLDER}/${ZIP_NAME}

rm -rf $TEMP_FOLDER

