package de.herhold.reactives3.reactiveClient.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.server")
@Data
public class ReactiveServerConfigProperties {
    private String url;
    private int inMemorySizeInMB;
}
