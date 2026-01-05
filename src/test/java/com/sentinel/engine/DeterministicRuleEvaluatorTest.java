package com.sentinel.engine;

import com.sentinel.model.Customer;
import com.sentinel.model.DeviceFingerprint;
import com.sentinel.model.Transaction;
import com.sentinel.repository.CustomerRepository;
import com.sentinel.service.VelocityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeterministicRuleEvaluatorTest {

    @Mock
    private VelocityService velocityService;
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private DeterministicRuleEvaluator evaluator;

    @Test
    void shouldReturnHighScoreWhenVelocityBreached() {
        Transaction txn = Transaction.builder()
            .entityId("entity-001")
            .amount(100.0)
            .build();

        when(velocityService.isVelocityBreached("entity-001")).thenReturn(Mono.just(true));
        when(customerRepository.findByEntityId("entity-001")).thenReturn(Mono.empty());

        StepVerifier.create(evaluator.evaluate(txn))
            .expectNextMatches(score -> score >= 40.0)
            .verifyComplete();
    }

    @Test
    void shouldDetectUnknownDevice() {
        DeviceFingerprint device = DeviceFingerprint.builder()
            .fingerprintHash("unknown-hash")
            .build();

        Transaction txn = Transaction.builder()
            .entityId("entity-002")
            .amount(100.0)
            .device(device)
            .build();

        Customer customer = Customer.builder()
            .entityId("entity-002")
            .knownDevices(List.of(DeviceFingerprint.builder()
                .fingerprintHash("known-hash").build()))
            .maxSingleTransaction(5000.0)
            .build();

        when(velocityService.isVelocityBreached(anyString())).thenReturn(Mono.just(false));
        when(customerRepository.findByEntityId("entity-002")).thenReturn(Mono.just(customer));

        StepVerifier.create(evaluator.evaluate(txn))
            .expectNextMatches(score -> score >= 30.0)
            .verifyComplete();
    }

    @Test
    void shouldReturnZeroForNormalTransaction() {
        Transaction txn = Transaction.builder()
            .entityId("entity-003")
            .amount(50.0)
            .build();

        Customer customer = Customer.builder()
            .entityId("entity-003")
            .maxSingleTransaction(5000.0)
            .build();

        when(velocityService.isVelocityBreached(anyString())).thenReturn(Mono.just(false));
        when(customerRepository.findByEntityId("entity-003")).thenReturn(Mono.just(customer));

        StepVerifier.create(evaluator.evaluate(txn))
            .expectNextMatches(score -> score == 0.0)
            .verifyComplete();
    }
}
