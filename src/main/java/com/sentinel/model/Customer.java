package com.sentinel.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "customers")
public class Customer {

    @Id
    private String id;

    @Indexed(unique = true)
    private String entityId;
    private String name;
    private String email;
    private String phone;
    private List<String> trustedMerchants;
    private List<String> frequentCategories;
    private double meanSpendingVelocity;
    private double maxSingleTransaction;
    private List<String> knownIpAddresses;
    private List<DeviceFingerprint> knownDevices;
    private List<GeoLocation> frequentLocations;
    private String riskTier;
    private int totalTransactionCount;
    private Instant accountCreatedAt;
    private Instant lastActivityAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public List<String> getTrustedMerchants() { return trustedMerchants; }
    public void setTrustedMerchants(List<String> trustedMerchants) { this.trustedMerchants = trustedMerchants; }
    public List<String> getFrequentCategories() { return frequentCategories; }
    public void setFrequentCategories(List<String> frequentCategories) { this.frequentCategories = frequentCategories; }
    public double getMeanSpendingVelocity() { return meanSpendingVelocity; }
    public void setMeanSpendingVelocity(double meanSpendingVelocity) { this.meanSpendingVelocity = meanSpendingVelocity; }
    public double getMaxSingleTransaction() { return maxSingleTransaction; }
    public void setMaxSingleTransaction(double maxSingleTransaction) { this.maxSingleTransaction = maxSingleTransaction; }
    public List<String> getKnownIpAddresses() { return knownIpAddresses; }
    public void setKnownIpAddresses(List<String> knownIpAddresses) { this.knownIpAddresses = knownIpAddresses; }
    public List<DeviceFingerprint> getKnownDevices() { return knownDevices; }
    public void setKnownDevices(List<DeviceFingerprint> knownDevices) { this.knownDevices = knownDevices; }
    public List<GeoLocation> getFrequentLocations() { return frequentLocations; }
    public void setFrequentLocations(List<GeoLocation> frequentLocations) { this.frequentLocations = frequentLocations; }
    public String getRiskTier() { return riskTier; }
    public void setRiskTier(String riskTier) { this.riskTier = riskTier; }
    public int getTotalTransactionCount() { return totalTransactionCount; }
    public void setTotalTransactionCount(int totalTransactionCount) { this.totalTransactionCount = totalTransactionCount; }
    public Instant getAccountCreatedAt() { return accountCreatedAt; }
    public void setAccountCreatedAt(Instant accountCreatedAt) { this.accountCreatedAt = accountCreatedAt; }
    public Instant getLastActivityAt() { return lastActivityAt; }
    public void setLastActivityAt(Instant lastActivityAt) { this.lastActivityAt = lastActivityAt; }
    public Customer() {}

    public static CustomerBuilder builder() { return new CustomerBuilder(); }
    public static class CustomerBuilder {
        private String id;
        public CustomerBuilder id(String id) { this.id = id; return this; }
        private String entityId;
        public CustomerBuilder entityId(String entityId) { this.entityId = entityId; return this; }
        private String name;
        public CustomerBuilder name(String name) { this.name = name; return this; }
        private String email;
        public CustomerBuilder email(String email) { this.email = email; return this; }
        private String phone;
        public CustomerBuilder phone(String phone) { this.phone = phone; return this; }
        private List<String> trustedMerchants;
        public CustomerBuilder trustedMerchants(List<String> trustedMerchants) { this.trustedMerchants = trustedMerchants; return this; }
        private List<String> frequentCategories;
        public CustomerBuilder frequentCategories(List<String> frequentCategories) { this.frequentCategories = frequentCategories; return this; }
        private double meanSpendingVelocity;
        public CustomerBuilder meanSpendingVelocity(double meanSpendingVelocity) { this.meanSpendingVelocity = meanSpendingVelocity; return this; }
        private double maxSingleTransaction;
        public CustomerBuilder maxSingleTransaction(double maxSingleTransaction) { this.maxSingleTransaction = maxSingleTransaction; return this; }
        private List<String> knownIpAddresses;
        public CustomerBuilder knownIpAddresses(List<String> knownIpAddresses) { this.knownIpAddresses = knownIpAddresses; return this; }
        private List<DeviceFingerprint> knownDevices;
        public CustomerBuilder knownDevices(List<DeviceFingerprint> knownDevices) { this.knownDevices = knownDevices; return this; }
        private List<GeoLocation> frequentLocations;
        public CustomerBuilder frequentLocations(List<GeoLocation> frequentLocations) { this.frequentLocations = frequentLocations; return this; }
        private String riskTier;
        public CustomerBuilder riskTier(String riskTier) { this.riskTier = riskTier; return this; }
        private int totalTransactionCount;
        public CustomerBuilder totalTransactionCount(int totalTransactionCount) { this.totalTransactionCount = totalTransactionCount; return this; }
        private Instant accountCreatedAt;
        public CustomerBuilder accountCreatedAt(Instant accountCreatedAt) { this.accountCreatedAt = accountCreatedAt; return this; }
        private Instant lastActivityAt;
        public CustomerBuilder lastActivityAt(Instant lastActivityAt) { this.lastActivityAt = lastActivityAt; return this; }
        public Customer build() {
            Customer instance = new Customer();
        instance.setId(this.id);
        instance.setEntityId(this.entityId);
        instance.setName(this.name);
        instance.setEmail(this.email);
        instance.setPhone(this.phone);
        instance.setTrustedMerchants(this.trustedMerchants);
        instance.setFrequentCategories(this.frequentCategories);
        instance.setMeanSpendingVelocity(this.meanSpendingVelocity);
        instance.setMaxSingleTransaction(this.maxSingleTransaction);
        instance.setKnownIpAddresses(this.knownIpAddresses);
        instance.setKnownDevices(this.knownDevices);
        instance.setFrequentLocations(this.frequentLocations);
        instance.setRiskTier(this.riskTier);
        instance.setTotalTransactionCount(this.totalTransactionCount);
        instance.setAccountCreatedAt(this.accountCreatedAt);
        instance.setLastActivityAt(this.lastActivityAt);
            return instance;
        }
    }

}
