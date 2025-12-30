package com.sentinel.engine;

import com.sentinel.model.Transaction;
import com.sentinel.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class VectorSearchEvaluator {

    private final EmbeddingService embeddingService;
    private final ReactiveMongoTemplate mongoTemplate;

    private static final double SIMILARITY_THRESHOLD = 0.85;
    private static final int SEARCH_LIMIT = 10;

    public Mono<Double> evaluate(Transaction transaction) {
        String input = embeddingService.buildEmbeddingInput(
            transaction.getMerchantName(),
            transaction.getMerchantCategory(),
            String.valueOf(transaction.getAmount()),
            transaction.getDevice() != null ? transaction.getDevice().getOs() : "unknown"
        );

        return embeddingService.generateEmbedding(input)
            .flatMap(embedding -> {
                if (embedding.isEmpty()) {
                    return Mono.just(0.0);
                }
                return executeVectorSearch(embedding);
            })
            .onErrorResume(e -> {
                log.warn("vector search evaluation failed", e);
                return Mono.just(0.0);
            });
    }

    private Mono<Double> executeVectorSearch(List<Double> queryVector) {
        Document searchStage = new Document("$vectorSearch", new Document()
            .append("index", "transaction_embeddings")
            .append("path", "embedding")
            .append("queryVector", queryVector)
            .append("numCandidates", SEARCH_LIMIT * 10)
            .append("limit", SEARCH_LIMIT)
        );

        Document projectStage = new Document("$project", new Document()
            .append("score", new Document("$meta", "vectorSearchScore"))
            .append("riskAssessment", 1)
        );

        return mongoTemplate.getCollection("transactions")
            .flatMapMany(collection ->
                collection.aggregate(Arrays.asList(searchStage, projectStage))
            )
            .collectList()
            .map(results -> {
                if (results.isEmpty()) return 0.0;
                long fraudMatches = results.stream()
                    .filter(doc -> {
                        Double score = doc.getDouble("score");
                        return score != null && score > SIMILARITY_THRESHOLD;
                    })
                    .filter(doc -> {
                        Document risk = doc.get("riskAssessment", Document.class);
                        return risk != null && "BLOCK".equals(risk.getString("action"));
                    })
                    .count();
                return Math.min(100.0, (fraudMatches / (double) SEARCH_LIMIT) * 100.0);
            });
    }
}
