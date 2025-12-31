package com.sentinel.engine;

import org.springframework.stereotype.Component;

@Component
public class RiskScoreAggregator {

    private static final double RULE_WEIGHT = 0.35;
    private static final double VECTOR_WEIGHT = 0.25;
    private static final double TOPOLOGY_WEIGHT = 0.40;

    private static final double ALLOW_THRESHOLD = 30.0;
    private static final double REVIEW_THRESHOLD = 70.0;

    public double aggregate(double ruleScore, double vectorScore, double topologyScore) {
        double weighted = (ruleScore * RULE_WEIGHT)
            + (vectorScore * VECTOR_WEIGHT)
            + (topologyScore * TOPOLOGY_WEIGHT);
        return Math.min(100.0, Math.max(0.0, weighted));
    }

    public String determineAction(double totalScore) {
        if (totalScore >= REVIEW_THRESHOLD) {
            return "BLOCK";
        } else if (totalScore >= ALLOW_THRESHOLD) {
            return "REVIEW";
        }
        return "ALLOW";
    }
}
