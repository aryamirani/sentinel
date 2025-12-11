package com.sentinel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
