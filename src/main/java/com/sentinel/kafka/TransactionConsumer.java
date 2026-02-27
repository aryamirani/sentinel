package com.sentinel.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sentinel.model.Transaction;
import com.sentinel.repository.TransactionRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Service
public class TransactionConsumer {
    public TransactionConsumer(TransactionRepository transactionRepository, ObjectMapper objectMapper) {
        this.transactionRepository = transactionRepository;
        this.objectMapper = objectMapper;
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TransactionConsumer.class);


    private final TransactionRepository transactionRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${sentinel.kafka.topic:transactions}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeTransaction(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String correlationId = extractCorrelationId(record);
        MDC.put("correlationId", correlationId);

        try {
            Transaction transaction = objectMapper.readValue(record.value(), Transaction.class);
            transaction.setUpdatedAt(Instant.now());

            transactionRepository.save(transaction)
                .doOnSuccess(saved -> {
                    log.info("persisted transaction id={} entity={}", saved.getId(), saved.getEntityId());
                    ack.acknowledge();
                })
                .doOnError(err -> log.error("failed to persist transaction", err))
                .subscribe();
        } catch (Exception e) {
            log.error("failed to deserialize transaction record", e);
            ack.acknowledge();
        } finally {
            MDC.remove("correlationId");
        }
    }

    private String extractCorrelationId(ConsumerRecord<String, String> record) {
        var header = record.headers().lastHeader("X-Correlation-ID");
        if (header != null) {
            return new String(header.value(), StandardCharsets.UTF_8);
        }
        return "unknown";
    }
}
