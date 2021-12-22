package de.herhold.reactives3.reactiveFileServer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.aws")
@Data
public class S3ClientConfigurarionProperties {
    private String region;
    private String accessKey;
    private String secretKey;
    private String endpoint;
    private String bucket;

}
