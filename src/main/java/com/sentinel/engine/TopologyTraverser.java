package com.sentinel.engine;

import com.sentinel.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TopologyTraverser {

    private final ReactiveMongoTemplate mongoTemplate;

    private static final int MAX_DEPTH = 5;
    private static final double CIRCULAR_LOOP_SCORE = 80.0;
    private static final double SHARED_IP_SCORE = 30.0;
    private static final double MULTI_HOP_SCORE = 50.0;

    public Mono<Double> evaluate(Transaction transaction) {
        return Mono.zip(
            detectCircularLoops(transaction),
            detectSharedInfrastructure(transaction)
        ).map(tuple -> Math.min(100.0, tuple.getT1() + tuple.getT2()));
    }

    // uses graphlookup to find circular payment loops
    private Mono<Double> detectCircularLoops(Transaction transaction) {
        if (transaction.getSenderAccountId() == null) {
            return Mono.just(0.0);
        }

        Document matchStage = new Document("$match",
            new Document("senderAccountId", transaction.getSenderAccountId()));

        Document graphLookup = new Document("$graphLookup", new Document()
            .append("from", "transactions")
            .append("startWith", "$receiverAccountId")
            .append("connectFromField", "receiverAccountId")
            .append("connectToField", "senderAccountId")
            .append("as", "paymentChain")
            .append("maxDepth", MAX_DEPTH)
            .append("depthField", "hopCount")
            .append("restrictSearchWithMatch",
                new Document("timestamp",
                    new Document("$gte", transaction.getTimestamp().minusSeconds(86400 * 7))))
        );

        return mongoTemplate.getCollection("transactions")
            .flatMapMany(collection ->
                collection.aggregate(Arrays.asList(matchStage, graphLookup))
            )
            .collectList()
            .map(results -> {
                for (Document result : results) {
                    List<Document> chain = result.getList("paymentChain", Document.class);
                    if (chain == null) continue;
                    boolean hasLoop = chain.stream()
                        .anyMatch(hop -> transaction.getSenderAccountId()
                            .equals(hop.getString("receiverAccountId")));
                    if (hasLoop) {
                        log.warn("circular payment loop detected for sender={}",
                            transaction.getSenderAccountId());
                        return CIRCULAR_LOOP_SCORE;
                    }
                    if (chain.size() >= 3) {
                        return MULTI_HOP_SCORE;
                    }
                }
                return 0.0;
            })
            .onErrorResume(e -> {
                log.warn("topology traversal failed", e);
                return Mono.just(0.0);
            });
    }

    private Mono<Double> detectSharedInfrastructure(Transaction transaction) {
        if (transaction.getIpAddress() == null) {
            return Mono.just(0.0);
        }

        Document matchStage = new Document("$match", new Document()
            .append("ipAddress", transaction.getIpAddress())
            .append("entityId", new Document("$ne", transaction.getEntityId()))
        );

        Document groupStage = new Document("$group", new Document()
            .append("_id", "$entityId")
        );

        return mongoTemplate.getCollection("transactions")
            .flatMapMany(collection ->
                collection.aggregate(Arrays.asList(matchStage, groupStage))
            )
            .collectList()
            .map(entities -> {
                if (entities.size() > 3) {
                    return SHARED_IP_SCORE;
                }
                return 0.0;
            });
    }
}
