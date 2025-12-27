package com.sentinel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingService {

    private final WebClient embeddingWebClient;

    @Value("${sentinel.embedding.model:text-embedding-3-small}")
    private String model;

    @SuppressWarnings("unchecked")
    public Mono<List<Double>> generateEmbedding(String input) {
        Map<String, Object> requestBody = Map.of(
            "model", model,
            "input", input
        );

        return embeddingWebClient.post()
            .uri("/v1/embeddings")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(Map.class)
            .map(response -> {
                List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
                return (List<Double>) data.get(0).get("embedding");
            })
            .onErrorResume(e -> {
                log.warn("embedding generation failed, returning empty vector", e);
                return Mono.just(List.of());
            });
    }

    public String buildEmbeddingInput(String merchantName, String category, String amount, String deviceOs) {
        return String.join(" | ", merchantName, category, amount, deviceOs);
    }
}
