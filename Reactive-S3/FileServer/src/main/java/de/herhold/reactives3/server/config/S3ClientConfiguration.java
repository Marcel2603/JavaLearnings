package de.herhold.reactives3.server.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.util.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(S3ClientConfigurarionProperties.class)
public class S3ClientConfiguration {
    @Bean
    public AmazonS3 s3client(S3ClientConfigurarionProperties s3props,
                             AWSCredentialsProvider credentials) {

        String region = s3props.getRegion();
        AmazonS3ClientBuilder amazonS3ClientBuilder = AmazonS3ClientBuilder.standard()
                .withCredentials(credentials);

        String endpoint = s3props.getEndpoint();
        if (!StringUtils.isNullOrEmpty(endpoint)) {
            System.out.println(region);
            amazonS3ClientBuilder.setEndpointConfiguration(
                    new AwsClientBuilder.EndpointConfiguration(endpoint, region)
            );
        } else {
            amazonS3ClientBuilder.withRegion(region);
        }
        return amazonS3ClientBuilder.build();
    }
}