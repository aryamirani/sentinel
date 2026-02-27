package com.sentinel.gateway;

import com.sentinel.kafka.TransactionProducer;
import com.sentinel.model.Transaction;
import com.sentinel.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private TransactionProducer transactionProducer;

    @MockitoBean
    private TransactionRepository transactionRepository;

    @Test
    void shouldAcceptTransaction() {
        when(transactionRepository.findByIdempotencyKey(any())).thenReturn(Mono.empty());

        webTestClient.post()
            .uri("/api/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("""
                {
                    "entityId": "entity-001",
                    "amount": 250.00,
                    "merchantName": "test merchant",
                    "currency": "USD"
                }
                """)
            .exchange()
            .expectStatus().isAccepted()
            .expectBody()
            .jsonPath("$.status").isEqualTo("accepted");
    }

    @Test
    void shouldReturnTransactions() {
        Transaction txn = Transaction.builder()
            .id("txn-001")
            .entityId("entity-001")
            .amount(100.0)
            .build();

        when(transactionRepository.findAllByOrderByTimestampDesc())
            .thenReturn(Flux.just(txn));

        webTestClient.get()
            .uri("/api/transactions?limit=10")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$[0].id").isEqualTo("txn-001");
    }

    @Test
    void shouldReturn404ForUnknownTransaction() {
        when(transactionRepository.findById("unknown")).thenReturn(Mono.empty());

        webTestClient.get()
            .uri("/api/transactions/unknown")
            .exchange()
            .expectStatus().isNotFound();
    }
}
