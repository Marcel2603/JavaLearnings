package de.herhold.reactives3.client.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.server")
@Data
public class ReactiveServerConfigProperties {
    private String url;
    private int inMemorySizeInMB;
}
