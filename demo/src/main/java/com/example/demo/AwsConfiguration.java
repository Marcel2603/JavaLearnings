package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

import java.net.URI;

@Configuration
@EnableAsync
public class AwsConfiguration {

    @Bean
    public DynamoDbClient dynamoDbClient() {
        DynamoDbClientBuilder builder = DynamoDbClient.builder();


        builder.endpointOverride(URI.create("http://localhost:4566"));
        builder.region(Region.EU_CENTRAL_1);
        builder.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("foo", "bar")));
        return builder.build();
    }
}
