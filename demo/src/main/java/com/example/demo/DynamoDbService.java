package com.example.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

import java.util.Map;

@Service
public class DynamoDbService {
    private final DynamoDbClient dynamoDbAsyncClient;


    public DynamoDbService(DynamoDbClient dynamoDbAsyncClient) {
        this.dynamoDbAsyncClient = dynamoDbAsyncClient;
    }

    @PostConstruct
    public void doSomething() {
        QueryResponse query = dynamoDbAsyncClient.query(QueryRequest.builder()
                .tableName("MusicCollection")
                .indexName("ApprovalCreatedAt")
                .keyConditionExpression("Approval = :v_not_set")
                .expressionAttributeValues(Map.of(":v_not_set", AttributeValue.fromS("NOT SET")))
                .scanIndexForward(true) // ascending sort
                .limit(100)
                .build());
        query.items().forEach(System.out::println);
    }
}
