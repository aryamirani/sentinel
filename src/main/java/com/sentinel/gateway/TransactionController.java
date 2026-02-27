package com.sentinel.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sentinel.kafka.TransactionProducer;
import com.sentinel.model.Transaction;
import com.sentinel.repository.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    public TransactionController(TransactionProducer transactionProducer, TransactionRepository transactionRepository, ObjectMapper objectMapper) {
        this.transactionProducer = transactionProducer;
        this.transactionRepository = transactionRepository;
        this.objectMapper = objectMapper;
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TransactionController.class);


    private final TransactionProducer transactionProducer;
    private final TransactionRepository transactionRepository;
    private final ObjectMapper objectMapper;

    @PostMapping
    public Mono<ResponseEntity<Map<String, String>>> ingestTransaction(
            @RequestBody Transaction transaction,
            @RequestHeader(value = "X-Idempotency-Key", required = false) String idempotencyKey,
            @RequestHeader(value = "X-Correlation-ID", required = false) String correlationId) {

        if (correlationId == null) {
            correlationId = UUID.randomUUID().toString();
        }

        if (idempotencyKey != null) {
            String key = idempotencyKey;
            return transactionRepository.findByIdempotencyKey(key)
                .map(existing -> ResponseEntity.ok(Map.of(
                    "transactionId", existing.getId(),
                    "status", "duplicate"
                )))
                .switchIfEmpty(processNewTransaction(transaction, key, correlationId));
        }

        return processNewTransaction(transaction, UUID.randomUUID().toString(), correlationId);
    }

    private Mono<ResponseEntity<Map<String, String>>> processNewTransaction(
            Transaction transaction, String idempotencyKey, String correlationId) {
        transaction.setIdempotencyKey(idempotencyKey);
        transaction.setCorrelationId(correlationId);
        transaction.setCreatedAt(Instant.now());
        transaction.setStatus("PENDING");

        try {
            String payload = objectMapper.writeValueAsString(transaction);
            transactionProducer.sendTransaction(transaction.getEntityId(), payload, correlationId);
            return Mono.just(ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of(
                "status", "accepted",
                "correlationId", correlationId
            )));
        } catch (JsonProcessingException e) {
            log.error("failed to serialize transaction", e);
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "serialization failed"
            )));
        }
    }

    @GetMapping
    public Flux<Transaction> getTransactions(
            @RequestParam(required = false) String entityId,
            @RequestParam(defaultValue = "50") int limit) {
        if (entityId != null) {
            return transactionRepository.findByEntityIdOrderByTimestampDesc(entityId)
                .take(limit);
        }
        return transactionRepository.findAllByOrderByTimestampDesc()
            .take(limit);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Transaction>> getTransaction(@PathVariable String id) {
        return transactionRepository.findById(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
