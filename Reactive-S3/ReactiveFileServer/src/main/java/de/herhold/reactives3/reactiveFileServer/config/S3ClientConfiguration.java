package de.herhold.reactives3.reactiveFileServer.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
@EnableConfigurationProperties(S3ClientConfigurarionProperties.class)
public class S3ClientConfiguration {
    @Bean
    public S3AsyncClient s3client(S3ClientConfigurarionProperties s3props,
                                  AwsCredentialsProvider credentialsProvider) {
        S3AsyncClientBuilder s3AsyncClientBuilder = S3AsyncClient.builder()
                .region(Region.of(s3props.getRegion()))
                .credentialsProvider(credentialsProvider);

        if (s3props.getEndpoint() != null) {
            s3AsyncClientBuilder = s3AsyncClientBuilder.endpointOverride(URI.create(s3props.getEndpoint()));
        }
        return s3AsyncClientBuilder.build();
    }

    @Bean
    public S3Presigner s3Presigner(S3ClientConfigurarionProperties s3props,
                                   AwsCredentialsProvider credentialsProvider) {
        S3Presigner.Builder builder = S3Presigner.builder()
                .region(Region.of(s3props.getRegion()))
                .credentialsProvider(credentialsProvider);

        if (s3props.getEndpoint() != null) {
            builder = builder.endpointOverride(URI.create(s3props.getEndpoint()));
        }
        return builder.build();
    }
}