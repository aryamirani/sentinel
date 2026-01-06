package com.sentinel.engine;

import com.sentinel.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.test.StepVerifier;

import java.time.Instant;

@ExtendWith(MockitoExtension.class)
class TopologyTraverserTest {

    @Mock
    private ReactiveMongoTemplate mongoTemplate;

    @InjectMocks
    private TopologyTraverser traverser;

    @Test
    void shouldReturnZeroWhenNoSenderAccount() {
        Transaction txn = Transaction.builder()
            .id("txn-001")
            .entityId("entity-001")
            .build();

        StepVerifier.create(traverser.evaluate(txn))
            .expectNextMatches(score -> score == 0.0)
            .verifyComplete();
    }

    @Test
    void shouldReturnZeroWhenNoIpAddress() {
        Transaction txn = Transaction.builder()
            .id("txn-002")
            .entityId("entity-002")
            .senderAccountId("acc-001")
            .timestamp(Instant.now())
            .build();

        StepVerifier.create(traverser.evaluate(txn))
            .expectNextCount(1)
            .verifyComplete();
    }
}
