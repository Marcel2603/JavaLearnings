#!/bin/bash

# start localstack
docker run -d -p 4566:4566 \
    -e SERVICES="s3" \
    -e DEBUG=1 \
    -e LS_LOG="info" \
    -e DEFAULT_REGION="eu-central-1" \
    --name localstack \
    localstack/localstack:latest

echo "Wait for localstack"
sleep 10

export AWS_ACCESS_KEY_ID=foo
export AWS_SECRET_ACCESS_KEY=bar
export DEFAULT_REGION=eu-central-1

aws --endpoint-url=http://localhost:4566 s3api create-bucket --bucket test-bucket --no-cli-pager
fallocate -l 10M 1gigfile
aws --endpoint-url=http://localhost:4566 s3 cp 1gigfile s3://test-bucket/1gigfile
rm 1gigfile
