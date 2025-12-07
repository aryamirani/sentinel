package com.sentinel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
