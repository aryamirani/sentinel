package com.sentinel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskAssessment {

    private double totalScore;
    private double ruleScore;
    private double vectorScore;
    private double topologyScore;
    private String action;
    private List<String> flags;
    private List<String> matchedPatterns;
    private long evaluationTimeMs;
}
