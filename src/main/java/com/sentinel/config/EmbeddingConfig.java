package com.sentinel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class EmbeddingConfig {

    @Value("${sentinel.embedding.api-key:}")
    private String apiKey;

    @Value("${sentinel.embedding.base-url:https://api.openai.com}")
    private String baseUrl;

    @Bean
    public WebClient embeddingWebClient() {
        return WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader("Authorization", "Bearer " + apiKey)
            .defaultHeader("Content-Type", "application/json")
            .build();
    }
}
