package com.sentinel.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "transactions")
@CompoundIndexes({
    @CompoundIndex(name = "entity_ts_idx", def = "{'entityId': 1, 'timestamp': -1}"),
    @CompoundIndex(name = "status_ts_idx", def = "{'status': 1, 'timestamp': -1}")
})
public class Transaction {

    @Id
    private String id;
    private String entityId;
    private String idempotencyKey;
    private String senderAccountId;
    private String receiverAccountId;
    private double amount;
    private String currency;
    private String merchantName;
    private String merchantCategory;
    private String ipAddress;
    private DeviceFingerprint device;
    private GeoLocation location;
    private Instant timestamp;
    private String status;
    private RiskAssessment riskAssessment;
    private List<Double> embedding;
    private String correlationId;
    private Instant createdAt;
    private Instant updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    public String getSenderAccountId() { return senderAccountId; }
    public void setSenderAccountId(String senderAccountId) { this.senderAccountId = senderAccountId; }
    public String getReceiverAccountId() { return receiverAccountId; }
    public void setReceiverAccountId(String receiverAccountId) { this.receiverAccountId = receiverAccountId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }
    public String getMerchantCategory() { return merchantCategory; }
    public void setMerchantCategory(String merchantCategory) { this.merchantCategory = merchantCategory; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public DeviceFingerprint getDevice() { return device; }
    public void setDevice(DeviceFingerprint device) { this.device = device; }
    public GeoLocation getLocation() { return location; }
    public void setLocation(GeoLocation location) { this.location = location; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public RiskAssessment getRiskAssessment() { return riskAssessment; }
    public void setRiskAssessment(RiskAssessment riskAssessment) { this.riskAssessment = riskAssessment; }
    public List<Double> getEmbedding() { return embedding; }
    public void setEmbedding(List<Double> embedding) { this.embedding = embedding; }
    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public Transaction() {}

    public static TransactionBuilder builder() { return new TransactionBuilder(); }
    public static class TransactionBuilder {
        private String id;
        public TransactionBuilder id(String id) { this.id = id; return this; }
        private String entityId;
        public TransactionBuilder entityId(String entityId) { this.entityId = entityId; return this; }
        private String idempotencyKey;
        public TransactionBuilder idempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; return this; }
        private String senderAccountId;
        public TransactionBuilder senderAccountId(String senderAccountId) { this.senderAccountId = senderAccountId; return this; }
        private String receiverAccountId;
        public TransactionBuilder receiverAccountId(String receiverAccountId) { this.receiverAccountId = receiverAccountId; return this; }
        private double amount;
        public TransactionBuilder amount(double amount) { this.amount = amount; return this; }
        private String currency;
        public TransactionBuilder currency(String currency) { this.currency = currency; return this; }
        private String merchantName;
        public TransactionBuilder merchantName(String merchantName) { this.merchantName = merchantName; return this; }
        private String merchantCategory;
        public TransactionBuilder merchantCategory(String merchantCategory) { this.merchantCategory = merchantCategory; return this; }
        private String ipAddress;
        public TransactionBuilder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        private DeviceFingerprint device;
        public TransactionBuilder device(DeviceFingerprint device) { this.device = device; return this; }
        private GeoLocation location;
        public TransactionBuilder location(GeoLocation location) { this.location = location; return this; }
        private Instant timestamp;
        public TransactionBuilder timestamp(Instant timestamp) { this.timestamp = timestamp; return this; }
        private String status;
        public TransactionBuilder status(String status) { this.status = status; return this; }
        private RiskAssessment riskAssessment;
        public TransactionBuilder riskAssessment(RiskAssessment riskAssessment) { this.riskAssessment = riskAssessment; return this; }
        private List<Double> embedding;
        public TransactionBuilder embedding(List<Double> embedding) { this.embedding = embedding; return this; }
        private String correlationId;
        public TransactionBuilder correlationId(String correlationId) { this.correlationId = correlationId; return this; }
        private Instant createdAt;
        public TransactionBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        private Instant updatedAt;
        public TransactionBuilder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }
        public Transaction build() {
            Transaction instance = new Transaction();
        instance.setId(this.id);
        instance.setEntityId(this.entityId);
        instance.setIdempotencyKey(this.idempotencyKey);
        instance.setSenderAccountId(this.senderAccountId);
        instance.setReceiverAccountId(this.receiverAccountId);
        instance.setAmount(this.amount);
        instance.setCurrency(this.currency);
        instance.setMerchantName(this.merchantName);
        instance.setMerchantCategory(this.merchantCategory);
        instance.setIpAddress(this.ipAddress);
        instance.setDevice(this.device);
        instance.setLocation(this.location);
        instance.setTimestamp(this.timestamp);
        instance.setStatus(this.status);
        instance.setRiskAssessment(this.riskAssessment);
        instance.setEmbedding(this.embedding);
        instance.setCorrelationId(this.correlationId);
        instance.setCreatedAt(this.createdAt);
        instance.setUpdatedAt(this.updatedAt);
            return instance;
        }
    }

}
