package com.sentinel.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sentinel.model.RiskAssessment;
import com.sentinel.model.Transaction;
import com.sentinel.repository.TransactionRepository;
import com.sentinel.service.AlertService;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RiskEngineTest {

    @Mock
    private DeterministicRuleEvaluator ruleEvaluator;
    @Mock
    private VectorSearchEvaluator vectorEvaluator;
    @Mock
    private TopologyTraverser topologyTraverser;
    @Mock
    private RiskScoreAggregator scoreAggregator;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AlertService alertService;

    @InjectMocks
    private RiskEngine riskEngine;

    private Transaction testTransaction;

    @BeforeEach
    void setUp() {
        testTransaction = Transaction.builder()
            .id("txn-001")
            .entityId("entity-001")
            .amount(5000.0)
            .build();
    }

    @Test
    void shouldEvaluateAllLayersAndReturnAssessedTransaction() {
        when(ruleEvaluator.evaluate(any())).thenReturn(Mono.just(20.0));
        when(vectorEvaluator.evaluate(any())).thenReturn(Mono.just(15.0));
        when(topologyTraverser.evaluate(any())).thenReturn(Mono.just(10.0));
        when(scoreAggregator.aggregate(20.0, 15.0, 10.0)).thenReturn(14.75);
        when(scoreAggregator.determineAction(14.75)).thenReturn("ALLOW");
        when(transactionRepository.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(riskEngine.evaluate(testTransaction))
            .expectNextMatches(txn -> {
                RiskAssessment ra = txn.getRiskAssessment();
                return ra != null
                    && ra.getTotalScore() == 14.75
                    && "ALLOW".equals(ra.getAction());
            })
            .verifyComplete();
    }

    @Test
    void shouldFlagHighRiskTransaction() {
        when(ruleEvaluator.evaluate(any())).thenReturn(Mono.just(80.0));
        when(vectorEvaluator.evaluate(any())).thenReturn(Mono.just(60.0));
        when(topologyTraverser.evaluate(any())).thenReturn(Mono.just(90.0));
        when(scoreAggregator.aggregate(80.0, 60.0, 90.0)).thenReturn(79.0);
        when(scoreAggregator.determineAction(79.0)).thenReturn("BLOCK");
        when(transactionRepository.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(riskEngine.evaluate(testTransaction))
            .expectNextMatches(txn ->
                "BLOCK".equals(txn.getRiskAssessment().getAction()))
            .verifyComplete();

        verify(alertService).evaluateAndAlert(any());
    }
}
