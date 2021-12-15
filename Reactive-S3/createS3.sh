#!/usr/bin/env sh
awslocal s3 mb "s3://${S3_BUCKET:-'test-bucket'}"