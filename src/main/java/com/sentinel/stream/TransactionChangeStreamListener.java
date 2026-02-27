package com.sentinel.stream;

import org.springframework.data.mongodb.core.ChangeStreamOptions;
import com.sentinel.engine.RiskEngine;
import com.sentinel.model.Transaction;
import org.bson.Document;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
public class TransactionChangeStreamListener {
    public TransactionChangeStreamListener(ReactiveMongoTemplate mongoTemplate, RiskEngine riskEngine) {
        this.mongoTemplate = mongoTemplate;
        this.riskEngine = riskEngine;
    }


    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TransactionChangeStreamListener.class);


    private final ReactiveMongoTemplate mongoTemplate;
    private final RiskEngine riskEngine;

    @EventListener(ApplicationReadyEvent.class)
    public void startListening() {
        log.info("starting transaction change stream listener");

        ChangeStreamOptions options = ChangeStreamOptions.builder()
            .filter(org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation(match(where("operationType").is("insert"))))
            .build();

        mongoTemplate.changeStream("transactions", options, Document.class)
            .doOnNext(this::handleChangeEvent)
            .doOnError(e -> log.error("change stream error", e))
            .subscribe();
    }

    private void handleChangeEvent(ChangeStreamEvent<Document> event) {
        Document body = event.getBody();
        if (body == null) return;

        try {
            String transactionId = body.getObjectId("_id").toString();
            log.debug("change stream detected new transaction={}", transactionId);

            Transaction transaction = mongoTemplate.getConverter().read(Transaction.class, body);
            riskEngine.evaluate(transaction)
                .subscribe(
                    result -> log.info("async risk evaluation completed for transaction={}", result.getId()),
                    error -> log.error("async risk evaluation failed", error)
                );
        } catch (Exception e) {
            log.error("failed to process change stream event", e);
        }
    }
}
