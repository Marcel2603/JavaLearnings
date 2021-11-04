# S3 Reactive Testing
This repo tests the download from s3 with a reactive spring app

# Usage
## S3
We work with Localstack. To create a simple localstack with a 1 Gigfile simply run `start_localstack.sh`

## Spring
simply run `mvn spring-boot:run`. It will start the app on port 9000

# Endpoints
## GET /download/{key}
This entpoint returns a file from s3 as byteArray. To run it with cURL you can execute following command
`curl localhost:9000/download/{key} --output file`

# Performance
For a 1Gigfile the services needs less than 150MB on Memory. The duration to download the file needs 3 seconds.
