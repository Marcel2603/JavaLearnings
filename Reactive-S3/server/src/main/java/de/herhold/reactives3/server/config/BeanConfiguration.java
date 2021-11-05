package de.herhold.reactives3.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.utils.StringUtils;

@Configuration
public class BeanConfiguration {
    @Bean
    public AwsCredentialsProvider awsCredentialsProvider(S3ClientConfigurarionProperties s3props) {
        if (StringUtils.isBlank(s3props.getAccessKey())) {
            return DefaultCredentialsProvider.create();
        } else {
            return () -> AwsBasicCredentials.create(
                    s3props.getAccessKey(),
                    s3props.getSecretKey());
        }
    }
}
