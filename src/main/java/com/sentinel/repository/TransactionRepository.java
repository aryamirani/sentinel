package com.sentinel.repository;

import com.sentinel.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {

    Flux<Transaction> findByEntityIdOrderByTimestampDesc(String entityId);

    Flux<Transaction> findAllByOrderByTimestampDesc();

    Mono<Transaction> findByIdempotencyKey(String idempotencyKey);

    Flux<Transaction> findBySenderAccountId(String senderAccountId);

    Flux<Transaction> findByReceiverAccountId(String receiverAccountId);

    Flux<Transaction> findByIpAddress(String ipAddress);
}
