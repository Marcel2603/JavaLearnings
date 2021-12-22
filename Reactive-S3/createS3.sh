#!/usr/bin/env sh

S3_BUCKET_NAME=${S3_BUCKET:-'test-bucket'}
echo "Create bucket $S3_BUCKET_NAME"
awslocal s3 mb "s3://${S3_BUCKET_NAME}"

echo "Create TestData"
head -c 1000m </dev/urandom > 1gigfile
head -c 10m </dev/urandom > 10Mb
head -c 100m </dev/urandom > 100Mb
awslocal s3 cp 1gigfile s3://${S3_BUCKET_NAME}/test/file1
awslocal s3 cp 1gigfile s3://${S3_BUCKET_NAME}/test/file2
awslocal s3 cp 10Mb s3://${S3_BUCKET_NAME}/test2/file1
awslocal s3 cp 10Mb s3://${S3_BUCKET_NAME}/test2/file2
awslocal s3 cp 1gigfile s3://${S3_BUCKET_NAME}/test3/file
awslocal s3 cp 100Mb s3://${S3_BUCKET_NAME}/test4/file

rm -f 1gigfile
rm -f 10Mb
rm -f 100Mb