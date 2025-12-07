package com.sentinel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
