package com.sentinel.service;

import com.sentinel.gateway.AlertWebSocketHandler;
import com.sentinel.model.AlertPayload;
import com.sentinel.model.RiskAssessment;
import com.sentinel.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertWebSocketHandler webSocketHandler;

    private static final double ALERT_THRESHOLD = 70.0;

    public void evaluateAndAlert(Transaction transaction) {
        RiskAssessment assessment = transaction.getRiskAssessment();
        if (assessment == null || assessment.getTotalScore() < ALERT_THRESHOLD) {
            return;
        }

        AlertPayload alert = AlertPayload.builder()
            .alertId(UUID.randomUUID().toString())
            .transactionId(transaction.getId())
            .entityId(transaction.getEntityId())
            .riskScore(assessment.getTotalScore())
            .action(assessment.getAction())
            .reason(buildReason(assessment))
            .amount(transaction.getAmount())
            .merchantName(transaction.getMerchantName())
            .ipAddress(transaction.getIpAddress())
            .timestamp(Instant.now())
            .build();

        log.info("broadcasting alert for transaction={} score={}",
            transaction.getId(), assessment.getTotalScore());
        webSocketHandler.broadcast(alert);
    }

    private String buildReason(RiskAssessment assessment) {
        if (assessment.getFlags() == null || assessment.getFlags().isEmpty()) {
            return "high risk score detected";
        }
        return String.join(", ", assessment.getFlags());
    }
}
