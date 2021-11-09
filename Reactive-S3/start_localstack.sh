#!/bin/bash
export AWS_ACCESS_KEY_ID=foo
export AWS_SECRET_ACCESS_KEY=bar
export DEFAULT_REGION=eu-central-1

aws --endpoint-url=http://localhost:4566 s3api create-bucket --bucket test-bucket --no-cli-pager
fallocate -l 1G 1gigfile
fallocate -l 10M 10Mb
aws --endpoint-url=http://localhost:4566 s3 cp 1gigfile s3://test-bucket/test/file1
aws --endpoint-url=http://localhost:4566 s3 cp 1gigfile s3://test-bucket/test/file2
aws --endpoint-url=http://localhost:4566 s3 cp 10Mb s3://test-bucket/test2/file2
aws --endpoint-url=http://localhost:4566 s3 cp 10Mb s3://test-bucket/test2/file2

rm -f 1gigfile
rm -f 10M
