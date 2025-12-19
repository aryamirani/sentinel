package com.sentinel.repository;

import com.sentinel.model.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {

    Mono<Customer> findByEntityId(String entityId);

    Flux<Customer> findByKnownIpAddressesContaining(String ipAddress);
}
