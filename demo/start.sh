#!/bin/bash
function dsi() {
    docker rm -f $(docker stop -t 0 $(docker ps -a | awk -v i="^$1.*" '{if($2~i){print$1}}'))
}


dsi localstack
echo "starting localstack"
docker run -d -p 4566:4566 -e SERVICE=dynamodb localstack/localstack

echo "wait 3 sec"
sleep 3

aws --endpoint-url=http://localhost:4566 --region=eu-central-1 --no-cli-pager dynamodb create-table \
    --table-name MusicCollection \
    --attribute-definitions AttributeName=SongTitle,AttributeType=S AttributeName=CreatedAt,AttributeType=S AttributeName=Approval,AttributeType=S \
    --key-schema AttributeName=SongTitle,KeyType=HASH AttributeName=CreatedAt,KeyType=RANGE \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --global-secondary-indexes \
            "[
                {
                    \"IndexName\": \"ApprovalCreatedAt\",
                    \"KeySchema\": [{\"AttributeName\":\"Approval\",\"KeyType\":\"HASH\"},
                                    {\"AttributeName\":\"CreatedAt\",\"KeyType\":\"RANGE\"}],
                    \"Projection\":{
                        \"ProjectionType\":\"INCLUDE\",
                        \"NonKeyAttributes\":[\"SongTitle\"]
                    },
                    \"ProvisionedThroughput\": {
                        \"ReadCapacityUnits\": 10,
                        \"WriteCapacityUnits\": 5
                    }
                }
            ]"

aws --endpoint-url=http://localhost:4566 --region=eu-central-1 --no-cli-pager dynamodb batch-write-item \
    --request-items file://test.json \
    --return-consumed-capacity INDEXES \
    --return-item-collection-metrics SIZE