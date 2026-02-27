package com.sentinel.model;


import java.time.Instant;

public class AlertPayload {

    private String alertId;
    private String transactionId;
    private String entityId;
    private double riskScore;
    private String action;
    private String reason;
    private double amount;
    private String merchantName;
    private String ipAddress;
    private Instant timestamp;

    public String getAlertId() { return alertId; }
    public void setAlertId(String alertId) { this.alertId = alertId; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }
    public double getRiskScore() { return riskScore; }
    public void setRiskScore(double riskScore) { this.riskScore = riskScore; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public AlertPayload() {}

    public static AlertPayloadBuilder builder() { return new AlertPayloadBuilder(); }
    public static class AlertPayloadBuilder {
        private String alertId;
        public AlertPayloadBuilder alertId(String alertId) { this.alertId = alertId; return this; }
        private String transactionId;
        public AlertPayloadBuilder transactionId(String transactionId) { this.transactionId = transactionId; return this; }
        private String entityId;
        public AlertPayloadBuilder entityId(String entityId) { this.entityId = entityId; return this; }
        private double riskScore;
        public AlertPayloadBuilder riskScore(double riskScore) { this.riskScore = riskScore; return this; }
        private String action;
        public AlertPayloadBuilder action(String action) { this.action = action; return this; }
        private String reason;
        public AlertPayloadBuilder reason(String reason) { this.reason = reason; return this; }
        private double amount;
        public AlertPayloadBuilder amount(double amount) { this.amount = amount; return this; }
        private String merchantName;
        public AlertPayloadBuilder merchantName(String merchantName) { this.merchantName = merchantName; return this; }
        private String ipAddress;
        public AlertPayloadBuilder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        private Instant timestamp;
        public AlertPayloadBuilder timestamp(Instant timestamp) { this.timestamp = timestamp; return this; }
        public AlertPayload build() {
            AlertPayload instance = new AlertPayload();
        instance.setAlertId(this.alertId);
        instance.setTransactionId(this.transactionId);
        instance.setEntityId(this.entityId);
        instance.setRiskScore(this.riskScore);
        instance.setAction(this.action);
        instance.setReason(this.reason);
        instance.setAmount(this.amount);
        instance.setMerchantName(this.merchantName);
        instance.setIpAddress(this.ipAddress);
        instance.setTimestamp(this.timestamp);
            return instance;
        }
    }

}
