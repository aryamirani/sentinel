package com.sentinel.model;


import java.util.List;

public class RiskAssessment {

    private double totalScore;
    private double ruleScore;
    private double vectorScore;
    private double topologyScore;
    private String action;
    private List<String> flags;
    private List<String> matchedPatterns;
    private long evaluationTimeMs;

    public double getTotalScore() { return totalScore; }
    public void setTotalScore(double totalScore) { this.totalScore = totalScore; }
    public double getRuleScore() { return ruleScore; }
    public void setRuleScore(double ruleScore) { this.ruleScore = ruleScore; }
    public double getVectorScore() { return vectorScore; }
    public void setVectorScore(double vectorScore) { this.vectorScore = vectorScore; }
    public double getTopologyScore() { return topologyScore; }
    public void setTopologyScore(double topologyScore) { this.topologyScore = topologyScore; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public List<String> getFlags() { return flags; }
    public void setFlags(List<String> flags) { this.flags = flags; }
    public List<String> getMatchedPatterns() { return matchedPatterns; }
    public void setMatchedPatterns(List<String> matchedPatterns) { this.matchedPatterns = matchedPatterns; }
    public long getEvaluationTimeMs() { return evaluationTimeMs; }
    public void setEvaluationTimeMs(long evaluationTimeMs) { this.evaluationTimeMs = evaluationTimeMs; }
    public RiskAssessment() {}

    public static RiskAssessmentBuilder builder() { return new RiskAssessmentBuilder(); }
    public static class RiskAssessmentBuilder {
        private double totalScore;
        public RiskAssessmentBuilder totalScore(double totalScore) { this.totalScore = totalScore; return this; }
        private double ruleScore;
        public RiskAssessmentBuilder ruleScore(double ruleScore) { this.ruleScore = ruleScore; return this; }
        private double vectorScore;
        public RiskAssessmentBuilder vectorScore(double vectorScore) { this.vectorScore = vectorScore; return this; }
        private double topologyScore;
        public RiskAssessmentBuilder topologyScore(double topologyScore) { this.topologyScore = topologyScore; return this; }
        private String action;
        public RiskAssessmentBuilder action(String action) { this.action = action; return this; }
        private List<String> flags;
        public RiskAssessmentBuilder flags(List<String> flags) { this.flags = flags; return this; }
        private List<String> matchedPatterns;
        public RiskAssessmentBuilder matchedPatterns(List<String> matchedPatterns) { this.matchedPatterns = matchedPatterns; return this; }
        private long evaluationTimeMs;
        public RiskAssessmentBuilder evaluationTimeMs(long evaluationTimeMs) { this.evaluationTimeMs = evaluationTimeMs; return this; }
        public RiskAssessment build() {
            RiskAssessment instance = new RiskAssessment();
        instance.setTotalScore(this.totalScore);
        instance.setRuleScore(this.ruleScore);
        instance.setVectorScore(this.vectorScore);
        instance.setTopologyScore(this.topologyScore);
        instance.setAction(this.action);
        instance.setFlags(this.flags);
        instance.setMatchedPatterns(this.matchedPatterns);
        instance.setEvaluationTimeMs(this.evaluationTimeMs);
            return instance;
        }
    }

}
