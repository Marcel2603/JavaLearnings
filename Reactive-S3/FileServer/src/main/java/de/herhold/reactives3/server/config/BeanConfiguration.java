package de.herhold.reactives3.server.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.util.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    public AWSCredentialsProvider awsCredentialsProvider(S3ClientConfigurarionProperties s3props) {
        if (StringUtils.isNullOrEmpty(s3props.getAccessKey())) {
            return new DefaultAWSCredentialsProviderChain();
        } else {
            return new AWSStaticCredentialsProvider(
                    new BasicAWSCredentials(
                            s3props.getAccessKey(),
                            s3props.getSecretKey())
            );
        }
    }
}
