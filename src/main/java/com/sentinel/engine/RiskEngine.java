package com.sentinel.engine;

import com.sentinel.model.RiskAssessment;
import com.sentinel.model.Transaction;
import com.sentinel.repository.TransactionRepository;
import com.sentinel.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RiskEngine {

    private final DeterministicRuleEvaluator ruleEvaluator;
    private final VectorSearchEvaluator vectorEvaluator;
    private final TopologyTraverser topologyTraverser;
    private final RiskScoreAggregator scoreAggregator;
    private final TransactionRepository transactionRepository;
    private final AlertService alertService;

    public Mono<Transaction> evaluate(Transaction transaction) {
        long startTime = System.currentTimeMillis();
        log.info("starting risk evaluation for transaction={}", transaction.getId());

        return Mono.zip(
            ruleEvaluator.evaluate(transaction),
            vectorEvaluator.evaluate(transaction),
            topologyTraverser.evaluate(transaction)
        ).map(tuple -> {
            double ruleScore = tuple.getT1();
            double vectorScore = tuple.getT2();
            double topologyScore = tuple.getT3();

            double totalScore = scoreAggregator.aggregate(ruleScore, vectorScore, topologyScore);
            String action = scoreAggregator.determineAction(totalScore);

            List<String> flags = new ArrayList<>();
            if (ruleScore > 30) flags.add("VELOCITY_ANOMALY");
            if (vectorScore > 30) flags.add("BEHAVIORAL_DEVIATION");
            if (topologyScore > 30) flags.add("TOPOLOGY_RISK");

            RiskAssessment assessment = RiskAssessment.builder()
                .totalScore(totalScore)
                .ruleScore(ruleScore)
                .vectorScore(vectorScore)
                .topologyScore(topologyScore)
                .action(action)
                .flags(flags)
                .evaluationTimeMs(System.currentTimeMillis() - startTime)
                .build();

            transaction.setRiskAssessment(assessment);
            transaction.setStatus(action);
            return transaction;
        }).flatMap(assessed ->
            transactionRepository.save(assessed)
                .doOnSuccess(saved -> {
                    alertService.evaluateAndAlert(saved);
                    log.info("risk evaluation complete transaction={} score={} action={}",
                        saved.getId(),
                        saved.getRiskAssessment().getTotalScore(),
                        saved.getRiskAssessment().getAction());
                })
        );
    }
}
