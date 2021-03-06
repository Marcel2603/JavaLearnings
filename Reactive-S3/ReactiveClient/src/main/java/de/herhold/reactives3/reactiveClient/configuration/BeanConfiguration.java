package de.herhold.reactives3.reactiveClient.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.herhold.reactives3.api.fileApi.reactiveClient.handler.ApiClient;
import de.herhold.reactives3.api.fileApi.reactiveClient.handler.FileApi;
import de.herhold.reactives3.api.fileApi.reactiveClient.handler.FileMemApi;
import de.herhold.reactives3.api.fileApi.reactiveClient.handler.RFC3339DateFormat;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.DateFormat;
import java.util.TimeZone;

@Configuration
@EnableConfigurationProperties(ReactiveServerConfigProperties.class)
public class BeanConfiguration {

    private final ReactiveServerConfigProperties reactiveServerConfigProperties;

    public BeanConfiguration(ReactiveServerConfigProperties reactiveServerConfigProperties) {
        this.reactiveServerConfigProperties = reactiveServerConfigProperties;
    }

    @Bean
    public FileApi fileApi() {
        ApiClient apiClient = new ApiClient(createWebClient(), createObjectMapper(), createDefaultDateFormat());
        apiClient.setBasePath(reactiveServerConfigProperties.getUrl());
        return new FileApi(apiClient);
    }

    @Bean
    public FileMemApi fileMemApi() {
        ApiClient apiClient = new ApiClient(createWebClient(), createObjectMapper(), createDefaultDateFormat());
        apiClient.setBasePath(reactiveServerConfigProperties.getUrl());
        return new FileMemApi(apiClient);
    }

    private WebClient createWebClient() {
        return WebClient.builder()
                .baseUrl(reactiveServerConfigProperties.getUrl())
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(reactiveServerConfigProperties.getInMemorySizeInMB() * 1000000))
                .build();
    }

    private DateFormat createDefaultDateFormat() {
        DateFormat dateFormat = new RFC3339DateFormat();
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat;
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(createDefaultDateFormat());
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNullableModule jnm = new JsonNullableModule();
        mapper.registerModule(jnm);
        return mapper;
    }
}
